import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {Course} from "../../models/course/course";
import {Chapter} from "../../models/course/chapter";
import {CoursesService} from "../../services/courses/courses.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink, MatIconModule],
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
      const idParam = params.get('id');
      const courseId = idParam ? +idParam : null;

      console.log("Course id = "+ courseId);

      if(courseId) {
        this.coursesService.getCourse(courseId).subscribe(course =>
        {
          this.course = course;
        });
        this.coursesService.getChapters(courseId).subscribe(chapters =>
        {
          this.coursesService.setFirstChapterVisible(chapters);
          this.chapters = chapters;
        });
      }
    })
  }

}
