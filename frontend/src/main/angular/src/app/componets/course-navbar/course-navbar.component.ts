import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Course} from "../../models/course";
import {MatIconModule} from "@angular/material/icon";
import {CoursesService} from "../../services/courses.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatButtonModule} from "@angular/material/button";
import { ShortChapter } from 'src/app/models/chapter';
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
  @Output() navchapters: EventEmitter<ShortChapter[]> = new EventEmitter();
  @Output() navCourse: EventEmitter<Course> = new EventEmitter();
  @Output() editModeChanged: EventEmitter<boolean> = new EventEmitter();
  showAdditionalMaterials: boolean = false;
  showChapters: boolean = false;
  shortChapters: ShortChapter[] = [];
  url!: string[];
  slug: string = '';
  currentChapter: number = 0;
  me: User;


  constructor(
    private coursesService: CoursesService,
    private tokenService: TokenStorageService,
    private route: ActivatedRoute
  ) {
    this.me = tokenService.getMe();
  }

  ngOnInit(): void {
      this.url = this.route.snapshot.url.map(urlSegment => urlSegment.path);
      this.slug = this.url[1];
      this.coursesService.getChapters(this.slug).subscribe(shortChapters =>{
        this.showChapters = shortChapters.length > 0;
        this.shortChapters = shortChapters;
        if (this.url.length == 2)
          this.navchapters.emit(shortChapters);
      });
      if (this.url.length >= 4)
          this.currentChapter = Number(this.url[3]) ;
      this.coursesService.getCourse(this.slug).subscribe(course => {
        if (course) {
          if (this.url.length == 3 && this.url[2] === 'main')
              this.navCourse.emit(course);
          this.showAdditionalMaterials = course.additionalMaterialsExist;
        }
      });
  }
 
  setEditMode(editMode: boolean) {
    this.editModeChanged.emit(editMode);
  }

  checkShowEdit(): boolean {
    return this.me.isMentor(this.slug)
  }

  checkShowApply(): boolean {
    return  !this.me.isStudent(this.slug) && !this.checkShowEdit();
  }

}

 