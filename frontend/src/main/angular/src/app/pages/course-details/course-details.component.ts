import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {Course} from "../../models/course/course";
import {Chapter} from "../../models/course/chapter";
import {CoursesService} from "../../services/courses/courses.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {ReportButtonComponent} from "../../componets/report-button/report-button.component";
import {ReportServiceService} from "../../services/report/report-service.service";
import {StudentReport} from "../../models/report/studentReport";
import {ApplyBtnComponent} from "../../componets/apply-btn/apply-btn.component";
import {EditBtnComponent} from 'src/app/componets/edit-btn/edit-course.component';
import {Practice} from "../../models/practice/practice";
import {TokenStorageService} from "../../services/auth/token-storage.service";
import {ChaptersService} from "../../services/chapters/chapters.service";
import {PracticeStatePipe} from "../../pipes/practice-state.pipe";

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink, MatIconModule, MatButtonModule, ReportButtonComponent,
     ApplyBtnComponent, EditBtnComponent, PracticeStatePipe],
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})
export class CourseDetailsComponent implements OnInit {
  course: Course | undefined;
  chapters: Chapter[] = [];
  reports: StudentReport[][]=[];
  slug: string='';
  practices: Practice[] = [];
  student: boolean = false;

  constructor(
      private coursesService: CoursesService,
      private route: ActivatedRoute,
      private reportService: ReportServiceService,
      private chaptersService: ChaptersService,
      private tokenStorageService: TokenStorageService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug');

      if(slug) {
        this.slug = slug;
        this.student = this.tokenStorageService.isStudent(slug);
        if (this.student)
          this.setPractices();
        this.coursesService.getChapters(slug).subscribe(chapters =>
            this.chapters = chapters
        );
        this.reportService.getAllActualReports(slug).subscribe(reports => {
            if (reports) {
              this.reports.push(...reports);
              this.reports = [...this.reports];
            }
        });
      }
    })
  }

  setPractices() {
    const practices = this.tokenStorageService.getPractice();
    if(practices){
      this.practices = practices;
    } else {
      this.chaptersService.getMyPractices().subscribe({
        next: value => {
          this.practices = value;
          this.tokenStorageService.updatePractice(value);
        }
      })
    }
  }

}