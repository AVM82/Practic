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
  imports: [CommonModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './course-navbar.component.html',
  styleUrls: ['./course-navbar.component.css']
})

export class CourseNavbarComponent implements OnInit {
  course: Course | undefined;
  chapters: Chapter[] = [];
  showChaptersLink: boolean = false;
  showAdditionalMaterials: boolean = false;
  slug: string = '';
  currentChapter: number = 0;

  constructor(
    private coursesService: CoursesService,
      private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug')
      this.currentChapter = Number(params.get('chapterN')) | 0;

      if (slug) {
        this.slug = slug;
        this.coursesService.getChapters(slug).subscribe(chapters => {
          this.chapters = chapters;
          this.showChaptersLink = chapters && chapters.length > 0;
        });
        this.coursesService.getAdditionalMaterialsExist(slug).subscribe(exist => 
           this.showAdditionalMaterials = exist
        );
      }
    })
  }
  
}

 