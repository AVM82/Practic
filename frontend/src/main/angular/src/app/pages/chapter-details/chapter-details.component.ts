import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
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
import {
  BUTTON_CONTINUE, BUTTON_FINISH, BUTTON_PAUSE, BUTTON_REPORT, BUTTON_RESULT_TEST, BUTTON_START, BUTTON_TEST,
  STATE_APPROVED, STATE_DONE, STATE_IN_PROCESS, STATE_NOT_STARTED, STATE_PAUSE, STATE_READY_TO_REVIEW
} from 'src/app/enums/app-constans';
import { ChapterPart } from 'src/app/models/chapterpart';
import { SubChapter } from 'src/app/models/subchapter';
import {QuizComponent} from "../../componets/quiz/quiz.component";
import {CertificateRequestComponent} from "../../componets/certificate-request-dialog/certificate-request.component";



@Component({
  selector: 'app-chapter-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent,  RouterLink, MatCardModule, MatIconModule, EditBtnComponent,
    CdkAccordionModule, MatTooltipModule, MatChipsModule, StatePipe, PracticeButtonsVisibilityPipe,
    ReportButtonComponent, QuizComponent, CertificateRequestComponent],
  templateUrl: './chapter-details.component.html',
  styleUrls: ['./chapter-details.component.css']
})
export class ChapterDetailsComponent implements OnInit {

  practices: Practice[] = [];
  isStudent: boolean = false;
  isMentor: boolean = false;
  chapter?: Chapter;
  isActiveChapter: boolean = false;
  showPartNumber: boolean = false;
  number: number = 0;
  slug: string = '';
  me!: User;
  readonly not_started = STATE_NOT_STARTED;
  readonly done = STATE_DONE;
  readonly process = STATE_IN_PROCESS;
  readonly pause = STATE_PAUSE;
  readonly ready = STATE_READY_TO_REVIEW;
  readonly approved = STATE_APPROVED;
  protected readonly BUTTON_RESULT_TEST = BUTTON_RESULT_TEST;
  service: StudentService;
  isQuizVisible: boolean = false;
  isQuizResultVisible: boolean = false;
  // passedTest?: boolean;

  constructor(
    public coursesService: CoursesService,
    private studentService: StudentService,
    private route: ActivatedRoute,
    private tokenStorageService: TokenStorageService,
    private router: Router
  ) {
    this.me = this.tokenStorageService.getMe();
    this.service = this.studentService;
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug')
      const number = Number(params.get('chapterN')) | 0;
      if (slug && number > 0)
        this.coursesService.getChapter(slug, number).subscribe(chapterObs => chapterObs.subscribe(chapter =>
          this.init(chapter, slug, number)));

    });
  }

  private init(chapter: Chapter, slug: string, number: number) {
      this.chapter = chapter;
      this.slug = slug;
      this.showPartNumber = this.chapter && this.chapter.parts.length > 1;
      this.isMentor = this.me.isMentor(slug);
      this.number = number;
      this.isActiveChapter = number === this.me.getCourseActiveChapterNumber(slug);
      this.isStudent = this.coursesService.stateStudent != undefined;
      if (this.isStudent) {
        this.studentService.setStudent(this.me.getStudent(slug)!);
        for(const part of chapter.parts)
          for(const sub of part.common!.subChapters)
            sub.checked = this.isSelected(sub.id);
      }
  }

  getTextAccordingState(): string {
    switch (this.chapter!.state) {
      case STATE_NOT_STARTED: return BUTTON_START;
      case STATE_PAUSE: return BUTTON_CONTINUE;
      case STATE_IN_PROCESS: 
         // if (this.notAllPracticesHaveBeenApproved())
         //    return BUTTON_PAUSE;
//          if (this.chapter!.myReports == 0)
//            return BUTTON_REPORT;
         if (!this.chapter!.isQuizPassed) return BUTTON_TEST;
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
      case BUTTON_START:  this.studentService.changeChapterState(this.chapter!, STATE_IN_PROCESS); 
                          break;
      case BUTTON_PAUSE:  this.studentService.changeChapterState(this.chapter!, STATE_PAUSE); 
                          break;
      case BUTTON_REPORT: this.router.navigate(['/courses', this.slug, 'reports']);
                          break;
      case BUTTON_TEST: this.isQuizVisible = true;
                          break;
      case BUTTON_FINISH: this.studentService.changeChapterState(this.chapter!, STATE_DONE);
                          break;
      default: console.error(' BUTTON failure : ', event.target.value);
    }
  }

  practiceReview(chapterPart: ChapterPart) {
    if (confirm('Всі вимоги практичної роботи виконані ?'))
      this.studentService.changePracticeState(this.chapter!.number, chapterPart, this.ready)
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
    // this.passedTest = true;
    this.isQuizResultVisible = false;
  }

  getResultQuiz() {
    this.isQuizVisible = false;
    this.isQuizResultVisible = true;
  }
}
