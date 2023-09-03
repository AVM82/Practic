import {Component, OnInit} from '@angular/core';
import {CommonModule, NgFor, NgIf} from '@angular/common';
import {Course} from "../../models/course/course";
import {MatIconModule} from "@angular/material/icon";
import {Chapter} from "../../models/course/chapter";
import {CoursesService} from "../../services/courses/courses.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'app-course-navbar',
  standalone: true,
  imports: [CommonModule, NgFor, NgIf, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './course-navbar.component.html',
  styleUrls: ['./course-navbar.component.css']
})

export class CourseNavbarComponent implements OnInit {
  course: Course | undefined;
  chapters: Chapter[] = [];
  slug: string = '';

  constructor(
      private coursesService: CoursesService,
      private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug')
      const chapterId = Number(params.get('chapterId')) | 0;

      if (slug) {
        this.slug = slug;
        this.coursesService.getCourse(slug).subscribe(course => {
          this.course = course;
          this.coursesService.getChapters(course.id).subscribe(chapters => {
            this.coursesService.setFirstChapterVisible(chapters);
            if(chapterId !== 0) {
              this.coursesService.setActiveChapter(chapters, chapterId);
            }else{
              this.coursesService.resetAllChapters(chapters);
            }
            this.chapters = chapters;
          });
        });
      }
    })
  }
}

 