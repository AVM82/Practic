import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {ReportButtonComponent} from "../../componets/report-button/report-button.component";
import {ReportServiceService} from "../../services/report-service.service";
import {StudentReport} from "../../models/studentReport";
import {Practice} from "../../models/practice";
import {TokenStorageService} from "../../services/token-storage.service";
import {ChaptersService} from "../../services/chapters.service";
import {StatePipe} from "../../pipes/practice-state.pipe";
import {Chapter} from 'src/app/models/chapter';
import { User } from 'src/app/models/user';
import { CoursesService } from 'src/app/services/courses.service';
import { StateStudent } from 'src/app/models/student';

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink,
     MatIconModule, MatButtonModule, ReportButtonComponent, StatePipe],
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})
export class CourseDetailsComponent implements OnInit {
  chapters: Chapter[] = [];
  reports: StudentReport[][]=[];
  slug: string='';
  practices: Practice[] = [];
  editMode: boolean = false;
  isStudent: boolean = false;
  isInvolved: boolean = false;
  stateStudent?: StateStudent;
  me!: User;

  constructor(
      private route: ActivatedRoute,
      private reportService: ReportServiceService,
      private coursesService: CoursesService,
      private chaptersService: ChaptersService,
      private tokenStorageService: TokenStorageService
  ) {
    this.me = this.tokenStorageService.getMe();
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug');
     if(slug) {
        this.slug = slug;
        this.stateStudent = this.me.getStudent(slug);
        this.isStudent = this.stateStudent != undefined;
        this.isInvolved = this.isStudent || this.me.isMentor(slug) || this.me.isGraduate(slug);
      }
    })
  }

  getChapters(chapters: Chapter[]) {
    this.chapters = chapters;
  }

  setEditMode(editMode: boolean) {
    this.editMode = editMode;
  }

  editClick(chapter: Chapter) {
    console.log('edit click on chapter #', chapter.number, '(id=', chapter.id, ')');
  }

}