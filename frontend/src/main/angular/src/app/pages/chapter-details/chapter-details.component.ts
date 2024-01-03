import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {Router, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {CdkAccordionModule} from '@angular/cdk/accordion';
import {MatTooltipModule} from "@angular/material/tooltip";
import {EditBtnComponent} from 'src/app/componets/edit-btn/edit-course.component';
import {MatChipsModule} from "@angular/material/chips";
import {StatePipe} from "../../pipes/practice-state.pipe";
import {PracticeButtonsVisibilityPipe} from "../../pipes/practice-btn-visibility.pipe";
import { Chapter } from 'src/app/models/chapter';
import { Practice } from 'src/app/models/practice';
import { User } from 'src/app/models/user';
import { CoursesService } from 'src/app/services/courses.service';
import { StudentService } from 'src/app/services/student.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { ReportButtonComponent } from 'src/app/componets/report-button/report-button.component';
import { BUTTON_CONTINUE, BUTTON_FINISH, BUTTON_PAUSE, BUTTON_REPORT, BUTTON_START, BUTTON_TEST, BUTTON_RESULT_TEST,
         STATE_ANNOUNCED, STATE_APPROVED, STATE_DONE, STATE_FINISHED, STATE_IN_PROCESS, STATE_NOT_STARTED,
    STATE_PAUSE, STATE_READY_TO_REVIEW, STATE_STARTED
       } from 'src/app/enums/app-constans';
import { ChapterPart } from 'src/app/models/chapterpart';
import { SubChapter } from 'src/app/models/subchapter';
import { ReportService } from 'src/app/services/report.service';
import {QuizComponent} from "../../componets/quiz/quiz.component";
import {CertificateRequestComponent} from "../../componets/certificate-request-dialog/certificate-request.component";
import { StateStudent } from 'src/app/models/student';



@Component({
  selector: 'app-chapter-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent,  RouterLink, MatCardModule, MatIconModule, EditBtnComponent,
    CdkAccordionModule, MatTooltipModule, MatChipsModule, StatePipe, PracticeButtonsVisibilityPipe,
    ReportButtonComponent, QuizComponent, CertificateRequestComponent],
  templateUrl: './chapter-details.component.html',
  styleUrls: ['./chapter-details.component.css']
})
export class ChapterDetailsComponent {

  practices: Practice[] = [];
  isStudent: boolean = false;
  isMentor: boolean = false;
  chapter?: Chapter;
  activeNumber: number = 0;
  showPartNumber: boolean = false;
  number: number = 0;
  slug: string = '';
  studentState?: StateStudent;
  me!: User;
  readonly STATE_NOT_STARTED = STATE_NOT_STARTED;
  readonly STATE_DONE = STATE_DONE;
  readonly STATE_IN_PROCESS = STATE_IN_PROCESS;
  readonly STATE_PAUSE = STATE_PAUSE;
  readonly STATE_READY_TO_REVIEW = STATE_READY_TO_REVIEW;
  readonly STATE_APPROVED = STATE_APPROVED;
  readonly STATE_ANNOUNCED = STATE_ANNOUNCED;
  readonly STATE_STARTED = STATE_STARTED;
  readonly STATE_FINISHED = STATE_FINISHED;
    readonly BUTTON_RESULT_TEST = BUTTON_RESULT_TEST;
  isQuizVisible: boolean = false;
  isQuizResultVisible: boolean = false;

  constructor(
    public coursesService: CoursesService,
    public studentService: StudentService,
    private reportService: ReportService,
    private tokenStorageService: TokenStorageService,
    private router: Router
  ) {
    this.me = this.tokenStorageService.getMe();
  }

  getSlug(slug: string) {
    this.slug = slug;
    this.isMentor = this.me.isMentor(slug);
    this.studentState = this.me.getStudent(slug);
    this.isStudent = this.studentState != undefined;
    this.activeNumber = this.isStudent ? this.me.getCourseActiveChapterNumber(slug) : 0;
  }

  getChapter(chapter: Chapter) {
      this.showPartNumber = chapter && chapter.parts.length > 1;
      if (this.coursesService.stateStudent != undefined)
        for(const part of chapter.parts)
          for(const sub of part.common!.subChapters)
            sub.checked = chapter.subs!.some(id => id === sub.id)
      this.chapter = chapter;
  }

  getTextAccordingState(): string {
    switch (this.chapter!.state) {
      case STATE_NOT_STARTED: return BUTTON_START;
      case STATE_PAUSE: return BUTTON_CONTINUE;
      case STATE_IN_PROCESS: 
        if (this.notAllPracticesHaveBeenApproved())
          return BUTTON_PAUSE;
        if (!this.reportService.reportsSubmitted(this.chapter!.myReports))
          return BUTTON_REPORT;
        if (!this.chapter!.quizPassed)
          return BUTTON_TEST;
        return BUTTON_FINISH;
      default: return '%#&$#^@&%(*&(*(+|}{}*';
    }
  }

  private notAllPracticesHaveBeenApproved(): boolean {
    return this.chapter!.parts.some(part => part.practice.state !== STATE_APPROVED);
  }

  changeState(event: any) {
    switch (event.target.innerText) {
      case BUTTON_CONTINUE:
      case BUTTON_START:  this.studentService.changeChapterState(this.chapter!, STATE_IN_PROCESS, this.studentState!);
                          break;
      case BUTTON_PAUSE:  this.studentService.changeChapterState(this.chapter!, STATE_PAUSE, this.studentState!);
                          break;
      case BUTTON_REPORT: this.router.navigate(['/courses', this.slug, 'reports']);
                          break;
      case BUTTON_TEST: this.isQuizVisible = true;
                          break;
      case BUTTON_FINISH: this.studentService.changeChapterState(this.chapter!, STATE_DONE, this.studentState!);
                          break;
      default: console.error(' BUTTON failure : ', event.target.value);
    }
  }

  practiceReview(chapterPart: ChapterPart) {
    if (confirm('Всі вимоги практичної роботи виконані ?'))
      this.studentService.changePracticeState(this.chapter!.number, chapterPart, STATE_READY_TO_REVIEW)
  }

  checkApproved(chapterPart: ChapterPart) {
    this.studentService.checkPracticeState(this.chapter!.number, chapterPart)
  }

  isSelected(subId: number): boolean {
    return this.chapter!.subs!.some(sub => sub === subId);
  }

  changeSkills(event: any, subchapter: SubChapter) {
    this.studentService.putSubChapterSkills(this.chapter!, subchapter, event);
  }

  closeQuiz() {
    this.isQuizVisible = false;
    this.isQuizResultVisible = false;
  }

  getResultQuiz() {
    this.isQuizVisible = false;
    this.isQuizResultVisible = true;
  }
}
