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

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink, MatIconModule, MatButtonModule],
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})
export class CourseDetailsComponent implements OnInit {
  course: Course | undefined;
  chapters: Chapter[] = [];

  constructor(
      private coursesService: CoursesService,
      private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const courseShortName = params.get('slug')

      if(courseShortName) {

        this.coursesService.getCourse(courseShortName).subscribe(course =>
        {
          this.course = course;
          this.coursesService.getChapters(course.id).subscribe(chapters =>
          {
            this.coursesService.setFirstChapterVisible(chapters);
            this.chapters = chapters;
          });
        });
      }
    })
  }

}
