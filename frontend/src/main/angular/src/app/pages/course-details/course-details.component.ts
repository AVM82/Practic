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

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink, MatIconModule, MatButtonModule, ReportButtonComponent],
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
      const courseShortName = params.get('slug')

      if(courseShortName) {
        this.slug = courseShortName;
        this.coursesService.getCourse(courseShortName).subscribe(course =>
        {
          this.course = course;
          this.coursesService.getChapters(course.id).subscribe(chapters =>
          {
            this.coursesService.setFirstChapterVisible(chapters);
            this.chapters = chapters;
          });
          this.reportService.getAllActualReports('java-dev-tools').subscribe(reports => {
            this.reports.push(...reports);
            this.reports = [...this.reports];
          });
        });
      }
    })
  }

}