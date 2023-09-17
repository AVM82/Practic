import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
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

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink, MatIconModule, MatButtonModule, ReportButtonComponent, ApplyBtnComponent],
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})
export class CourseDetailsComponent implements OnInit {
  course: Course | undefined;
  chapters: Chapter[] = [];
  reports: StudentReport[][]=[];
  slug: string='';

  constructor(
      private coursesService: CoursesService,
      private route: ActivatedRoute,
      private reportService: ReportServiceService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug')

      if(slug) {
        this.slug = slug;
        this.coursesService.getCourse(slug).subscribe(course =>
        {
          this.course = course;
          this.coursesService.getChapters(slug).subscribe(chapters =>
          {
            //this.coursesService.setFirstChapterVisible(chapters);
            this.chapters = chapters;
            this.setChapterVisibility();
          });
          this.reportService.getAllActualReports(slug).subscribe(reports => {
            this.reports.push(...reports);
            this.reports = [...this.reports];
          });
        });
      }
    })
  }

  private setChapterVisibility() :void {
    this.coursesService.getOpenChapters().subscribe({
      next: chapters => {
        this.coursesService.setVisibleChapters(this.chapters, chapters);
      },
      error: error => {
        console.error('Помилка при запиті доступних глав', error);
      }
    })
  }

}