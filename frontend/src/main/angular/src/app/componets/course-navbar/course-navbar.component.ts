import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Course} from "../../models/course";
import {MatIconModule} from "@angular/material/icon";
import {CoursesService} from "../../services/courses.service";
import {ActivatedRoute, ParamMap, RouterLink} from "@angular/router";
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
  @Output() navSlug: EventEmitter<string> = new EventEmitter();
  @Output() currentChapter: EventEmitter<Chapter> = new EventEmitter();
  showAdditionalMaterials: boolean = false;
  showChapters: boolean = false;
  chapters: Chapter[] = [];
  slug: string = '';
  showEditButton: boolean =false;
  currentNumber: number = 0;
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
      const slug = params.get('slug');
      if (slug) {
        this.slug = slug;
        this.navSlug.emit(slug);
        this.loadCourse(slug);
        this.loadChapters(slug, params);
     }
    }) 
  }
 
  private loadCourse(slug: string): void {
    this.coursesService.getCourse(this.slug).subscribe(course => {
      if (course) {
        this.navCourse.emit(course);
        this.showAdditionalMaterials = course.additionalMaterialsExist;
      }
    });
  }

  private loadChapters(slug: string, params: ParamMap): void {
    this.coursesService.getChapters(this.slug).subscribe(shortChapters =>{
      if (shortChapters) {
        this.showChapters = shortChapters.length > 0;
        this.chapters = shortChapters;
        this.navchapters.emit(shortChapters);
      }
      let url = this.route.snapshot.url;
      this.showEditButton = this.me.isMentor(this.slug) && (url.length !== 3 || url[2].path !== 'reports');
      if (url.length == 4 && url[2].path === 'chapters') {
        const number = Number(params.get('chapterN')) | 0;
        const chapter = shortChapters.find(shortChapters  => shortChapters.number === number);
        if (chapter)
          this.coursesService.extChapter(chapter).subscribe(chapter => {
              this.currentChapter.emit(chapter);
              this.currentNumber = chapter.number;
            } )
      }
    });
  }

  setEditMode(editMode: boolean) {
    this.editModeChanged.emit(editMode);
  }

}

 