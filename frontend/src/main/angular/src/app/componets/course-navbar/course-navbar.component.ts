import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Course} from "../../models/course/course";
import {MatIconModule} from "@angular/material/icon";
import {CoursesService} from "../../services/courses/courses.service";
import {ActivatedRoute, RouterLink, UrlSegment} from "@angular/router";
import {MatButtonModule} from "@angular/material/button";
import { ShortChapter } from 'src/app/models/course/chapter';
import { ApplyBtnComponent } from '../apply-btn/apply-btn.component';
import { EditBtnComponent } from '../edit-btn/edit-course.component';
import { User } from 'src/app/models/user/user';
import { StudentService } from 'src/app/services/student/student.service';
import { TokenStorageService } from 'src/app/services/auth/token-storage.service';
import { environment } from 'src/enviroments/enviroment';

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
  @Output() editModeChanged: EventEmitter<boolean> = new EventEmitter();
  showEdit: boolean = false;
  showApply: boolean = false;
  chapters: ShortChapter[] = [];
  showAdditionalMaterials: boolean = false;
  url!: string[];
  slug: string = '';
  currentChapter: number = 0;
  me: User;


  constructor(
    private coursesService: CoursesService,
    private studentService: StudentService,
    private tokenService: TokenStorageService,
    private route: ActivatedRoute
  ) {
    this.me = tokenService.getMe();
  }

  ngOnInit(): void {
    if (this.me) {
      //this.route.paramMap.subscribe(params => {

        this.url = this.route.snapshot.url.map(urlSegment => urlSegment.path);
        this.slug = this.url[1];
        if (this.url.length >= 4)
          this.currentChapter = Number(this.url[3]) ;
        
      console.log('path url segments = ', this.url);
  
          const student = this.me.isStudent(this.slug);
          this.showEdit = this.me.isMentor(this.slug);
          this.showApply = !student && !this.showEdit;
          this.coursesService.getCourse(this.slug).subscribe(course => {
            if (this.url.length == 3 && this.url[2] === 'main')
              this.navCourse.emit(course);
            this.showAdditionalMaterials = course.additionalMaterialsExist;
          });
          if (student)
            this.studentService.getStudentChapters(this.slug).subscribe(chapters =>
              this.setChapters(chapters));
          else
            this.coursesService.getChapters(this.slug).subscribe(chapters =>
              this.setChapters(chapters));
        
//      })
    }
  }
 
  setChapters(chapters: ShortChapter[]): void {
    this.chapters = chapters;
    if (chapters && this.url.length == 2)
        this.navchapters.emit(chapters);
  }

  setEditMode(editMode: boolean) {
    this.editModeChanged.emit(editMode);
  }

}

 