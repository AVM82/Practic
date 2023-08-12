import {Component, OnInit} from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import {Course} from "../../models/course/course";
import {CoursesService} from "../../services/courses/courses.service";
import {MatIconModule} from "@angular/material/icon";
import {ActivatedRoute, Router} from "@angular/router";
import {Chapter} from "../../models/course/chapter";

@Component({
  selector: 'app-course-navbar',
  standalone: true,
  imports: [CommonModule, NgFor, NgIf, MatIconModule],
  templateUrl: './course-navbar.component.html',
  styleUrls: ['./course-navbar.component.css']
})
export class CourseNavbarComponent implements OnInit{
  course: Course | undefined;
  chapters: Chapter[] = [];

  constructor(
      private coursesService: CoursesService,
      private route: ActivatedRoute,
      private router: Router
  ) {}

  ngOnInit(): void {
     this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      const courseId = idParam ? +idParam : null;

      console.log("Course id = "+ courseId);

      if(courseId) {
        this.coursesService.getCourse(courseId).subscribe(
            course => {
                      this.course = course;
                      },
            error => {
                      if(error.status === 404){
                        this.router.navigateByUrl('/404')
                      }
            });
        this.coursesService.getChapters(courseId).subscribe(chapters => {
          this.coursesService.setFirstChapterVisible(chapters);
          this.chapters = chapters;
        });
      }
    })
  }

  navigateToChapter(chapter: Chapter): void {
    console.log(chapter);
  }
}

