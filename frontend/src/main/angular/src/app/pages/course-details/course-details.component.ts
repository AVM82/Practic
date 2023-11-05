import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {ReportButtonComponent} from "../../componets/report-button/report-button.component";
import {ReportServiceService} from "../../services/report-service.service";
import {StudentReport} from "../../models/studentReport";
import {Practice} from "../../models/practice";
import {TokenStorageService} from "../../services/token-storage.service";
import {ChaptersService} from "../../services/chapters.service";
import {PracticeStatePipe} from "../../pipes/practice-state.pipe";
import {ShortChapter} from 'src/app/models/chapter';
import { User } from 'src/app/models/user';
import { CoursesService } from 'src/app/services/courses.service';

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink,
     MatIconModule, MatButtonModule, ReportButtonComponent, PracticeStatePipe],
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})
export class CourseDetailsComponent implements OnInit {
  chapters: ShortChapter[] = [];
  reports: StudentReport[][]=[];
  slug: string='';
  practices: Practice[] = [];
  student: boolean = false;
  editMode: boolean = false;
  me!: User;

  constructor(
      private route: ActivatedRoute,
      private reportService: ReportServiceService,
      private coursesService: CoursesService,
      private chaptersService: ChaptersService,
      private tokenStorageService: TokenStorageService
  ) {
    this.me = this.tokenStorageService.getMe();
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug');

      if(slug) {
        this.slug = slug;
        this.student = this.me.isStudent(slug);
        this.reportService.getAllActualReports(slug).subscribe(reports => {
            if (reports) {
              this.reports.push(...reports);
              this.reports = [...this.reports];
            }
        });
//        if (this.student)
//          this.setPractices();

      }
    })
  }

  getChapters(chapters: ShortChapter[]) {
    this.chapters = chapters;
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

  setEditMode(editMode: boolean) {
    this.editMode = editMode;
  }

  editClick(chapter: ShortChapter) {
    console.log('edit click on chapter #', chapter.number, '(id=', chapter.id, ')');
  }

}