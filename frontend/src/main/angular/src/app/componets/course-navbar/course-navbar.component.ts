import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Course} from "../../models/course";
import {MatIconModule} from "@angular/material/icon";
import {CoursesService} from "../../services/courses.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatButtonModule} from "@angular/material/button";
import { Chapter } from 'src/app/models/chapter';
import { ApplyBtnComponent } from '../apply-btn/apply-btn.component';
import { EditBtnComponent } from '../edit-btn/edit-course.component';
import { User } from 'src/app/models/user';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-course-navbar',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, RouterLink, ApplyBtnComponent, EditBtnComponent],
  templateUrl: './course-navbar.component.html',
  styleUrls: ['./course-navbar.component.css']
})

export class CourseNavbarComponent implements OnInit {
  @Output() navchapters: EventEmitter<Chapter[]> = new EventEmitter();
  @Output() navCourse: EventEmitter<Course> = new EventEmitter();
  @Output() editModeChanged: EventEmitter<boolean> = new EventEmitter();
  @Input() currentChapter: number = 0;
  showAdditionalMaterials: boolean = false;
  showChapters: boolean = false;
  chapters: Chapter[] = [];
  slug: string = '';
  me: User;


  constructor(
    private coursesService: CoursesService,
    private tokenService: TokenStorageService,
    private route: ActivatedRoute
  ) {
    this.me = tokenService.getMe();
  }

  ngOnInit(): void {   
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug')
      if (slug) {
        this.slug = slug;
        this.coursesService.getChapters(this.slug).subscribe(shortChapters =>{
          if (shortChapters) {
            this.showChapters = shortChapters.length > 0;
            this.chapters = shortChapters;
            this.navchapters.emit(shortChapters);
          }
        });
        this.coursesService.getCourse(this.slug).subscribe(course => {
          if (course) {
            this.navCourse.emit(course);
            this.showAdditionalMaterials = course.additionalMaterialsExist;
          }
        });
     }
    }) 
  }
 
  setEditMode(editMode: boolean) {
    this.editModeChanged.emit(editMode);
  }

}

 