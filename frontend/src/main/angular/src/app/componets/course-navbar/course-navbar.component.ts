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
  showChaptersLink: boolean = false;
  showAdditionalMaterials: boolean = false;
  slug: string = '';

  constructor(
      private coursesService: CoursesService,
      private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug')
      const chapterN = Number(params.get('chapterN')) | 0;

      if (slug) {
        this.slug = slug;
        this.coursesService.getCourse(slug).subscribe(course => {
          this.course = course;
          this.coursesService.getChapters(slug).subscribe(chapters => {
            this.showChaptersLink = chapters.length > 0;
            this.coursesService.setFirstChapterVisible(chapters);
            if(chapterN !== 0) {
              this.coursesService.setActiveChapter(chapters, chapterN);
            }else{
              this.coursesService.resetAllChapters(chapters);
            }
            this.chapters = chapters;
          });
          this.coursesService.getAdditionalMaterials(slug).subscribe(additionalMaterials => {
            this.showAdditionalMaterials = additionalMaterials.length > 0;
          });
        });
      }
    })
  }
}

 