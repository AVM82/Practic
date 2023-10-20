import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Course} from "../../models/course/course";
import {MatIconModule} from "@angular/material/icon";
import {CoursesService} from "../../services/courses/courses.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatButtonModule} from "@angular/material/button";
import { ShortChapter } from 'src/app/models/course/chapter';
import { ApplyBtnComponent } from '../apply-btn/apply-btn.component';
import { EditBtnComponent } from '../edit-btn/edit-course.component';

@Component({
  selector: 'app-course-navbar',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, RouterLink, ApplyBtnComponent, EditBtnComponent],
  templateUrl: './course-navbar.component.html',
  styleUrls: ['./course-navbar.component.css']
})

export class CourseNavbarComponent implements OnInit {
  course: Course | undefined;
  @Output() navchapters: EventEmitter<ShortChapter[]> = new EventEmitter();
  @Output() navCourse: EventEmitter<Course> = new EventEmitter();
  showEdit: boolean = false;
  showApply: boolean = false;
  chapters: ShortChapter[] = [];
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
        this.coursesService.getCourse(slug).subscribe(course => {
          this.showEdit = this.coursesService.amIAmongOthers(course.mentors);
          this.showApply = !this.coursesService.isStudent && !this.showEdit;
          if (this.currentChapter == 0)
            this.navCourse.emit(course);
          this.showAdditionalMaterials = course.additionalMaterialsExist;
        });
        this.coursesService.getChapters(slug).subscribe(chapters => {
          this.chapters = chapters;
          if (chapters) {
            if (this.currentChapter == 0)
              this.navchapters.emit(chapters);
          }
        });
      }
    })
  }
  
}

 