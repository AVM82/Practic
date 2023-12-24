import {Component} from '@angular/core';
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
import {StatePipe} from "../../pipes/practice-state.pipe";
import {Chapter} from 'src/app/models/chapter';
import { User } from 'src/app/models/user';
import { Report } from 'src/app/models/report';
import { StateStudent } from 'src/app/models/student';
import { ReportState } from 'src/app/enums/app-constans';

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink,
     MatIconModule, MatButtonModule, ReportButtonComponent, StatePipe],
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})
export class CourseDetailsComponent {
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
      public reportService: ReportServiceService,
      private tokenStorageService: TokenStorageService
  ) {
    this.me = this.tokenStorageService.getMe();
  }

  getSlug(slug: string) {
    this.slug = slug;
    this.stateStudent = this.me.getStudent(slug);
    this.isStudent = this.stateStudent != undefined;
    this.isInvolved = this.isStudent || this.me.isMentor(slug) || this.me.isGraduate(slug);
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

  getReports(myReports: Report[]): string {
    let number = myReports.filter(report => report.state === ReportState.APPROVED).length;
    return number === 1 ? '1 проведена' 
            : ((number === 0 ? 'не' : number) + ' проведено');
  }
  
}