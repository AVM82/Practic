import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {CdkAccordionModule} from '@angular/cdk/accordion';
import {MatTooltipModule} from "@angular/material/tooltip";
import {InfoMessagesService} from "../../services/info-messages.service";
import {EditBtnComponent} from 'src/app/componets/edit-btn/edit-course.component';
import {MatChipsModule} from "@angular/material/chips";
import {StatePipe} from "../../pipes/practice-state.pipe";
import {PracticeButtonsVisibilityPipe} from "../../pipes/practice-btn-visibility.pipe";
import { Chapter, ShortChapter } from 'src/app/models/chapter';
import { Practice } from 'src/app/models/practice';
import { User } from 'src/app/models/user';
import { CoursesService } from 'src/app/services/courses.service';
import { StudentService } from 'src/app/services/student.service';
import { ChaptersService } from 'src/app/services/chapters.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { ChapterPart } from 'src/app/models/chapterpart';
import { ReportButtonComponent } from 'src/app/componets/report-button/report-button.component';
import { BUTTON_CONTINUE, BUTTON_FINISH, BUTTON_PAUSE, BUTTON_REPORT, BUTTON_START,
         STATE_APPROVED, STATE_DONE, STATE_IN_PROCESS, STATE_NOT_STARTED, STATE_PAUSE } from 'src/app/enums/app-constans';



@Component({
  selector: 'app-chapter-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent,  RouterLink, MatCardModule, MatIconModule, EditBtnComponent,
    CdkAccordionModule, MatTooltipModule, MatChipsModule, StatePipe, PracticeButtonsVisibilityPipe,
    ReportButtonComponent],
  templateUrl: './chapter-details.component.html',
  styleUrls: ['./chapter-details.component.css']
})
export class ChapterDetailsComponent implements OnInit {

  practices: Practice[] = [];
  isStudent: boolean = false;
  isMentor: boolean = false;
  shortChapters: ShortChapter[] = [];
  chapter?: Chapter;
  isActive: boolean = false;
  showPartNumber: boolean = false;
  number: number = 0;
  slug: string = '';
  me!: User;
  readonly done = STATE_DONE;

  constructor(
    private coursesService: CoursesService,
    private studentService: StudentService,
    private chaptersService: ChaptersService,
    private route: ActivatedRoute,
    private messagesService: InfoMessagesService,
    private tokenStorageService: TokenStorageService,
    private router: Router
  ) {
    this.me = this.tokenStorageService.getMe();
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug')
      const number = Number(params.get('chapterN')) | 0;
      if (slug && number > 0)
        this.coursesService.getChapter(slug, number).subscribe(chapter => {
          this.chapter = chapter;
          this.slug = slug;
          this.showPartNumber = this.chapter && this.chapter.parts.length > 1;
          this.isMentor = this.me.isMentor(slug);
          this.number = number;
          this.isStudent = this.coursesService.stateStudent != undefined;
          if (this.isStudent)
            this.studentService.setStudent(this.me.getStudent(slug)!);
        });

    });
  }

  getTextAccordingState(): string {
    switch (this.chapter!.state) {
      case STATE_NOT_STARTED: return BUTTON_START;
      case STATE_PAUSE: return BUTTON_CONTINUE;
      case STATE_IN_PROCESS: 
//         if (!this.practicesHaveBeenApproved())
//            return BUTTON_PAUSE;
//          if (this.chapter!.myReports == 0)
//            return BUTTON_REPORT;
//          if (!this.chapter!.testIsPassed)
//            return 'ТЕСТ';
          return BUTTON_FINISH;
      default: return '%#&$#^@&%(*&(*(+|}{}*';
    }
  }

  private practicesHaveBeenApproved(): boolean {
    return this.chapter!.practices.filter(prac => prac.state === STATE_APPROVED).length == this.chapter!.parts.length;
  }

  getPracticeState(chapterPartId: number): string | undefined {
    return this.chapter!.practices?.find(practice => practice.chapterPartId === chapterPartId)?.state;
  }
  
  changeState(event: any) {
    console.log(event);
    switch (event.target.innerText) {
      case BUTTON_CONTINUE:
      case BUTTON_START:  this.studentService.changeChapterState(this.chapter!, STATE_IN_PROCESS); 
                          break;
      case BUTTON_PAUSE:  this.studentService.changeChapterState(this.chapter!, STATE_PAUSE); 
                          break;
      case BUTTON_PAUSE:  this.studentService.changeChapterState(this.chapter!, STATE_DONE); 
                          break;
      case BUTTON_REPORT: this.router.navigate(['/courses', this.slug, 'reports']);
                          break;
      case BUTTON_FINISH: this.studentService.changeChapterState(this.chapter!, STATE_DONE);
                          break;
      default: console.error(' BUTTON failure : ', event.target.value);
    }
  }

  setPractices() {
 /*   const practices = this.tokenStorageService.getPractice();
    if(practices){
      this.practices = practices;
    } else {
      this.updatePractices();
    }
  */  }

  updatePractices() {
/*    this.chaptersService.getMyPractices().subscribe({
      next: value => {
        this.practices = value;
        this.tokenStorageService.updatePractice(value);
      }
    })
  */ }

  playAction(chapterPart: ChapterPart) {
    this.chaptersService.setPracticeState('IN_PROCESS', chapterPart.id).subscribe({
      next: () => {
        this.updatePractices();
        this.messagesService.showMessage("Стан практичної змінено на стан 'В ПРОЦЕССІ'", "normal");
      },
      error: err => {
        this.showError(err);
      }
    });
  }

  pauseAction(chapterPart: ChapterPart) {
    this.chaptersService.setPracticeState('PAUSE', chapterPart.id).subscribe({
      next: () => {
        this.updatePractices();
        this.messagesService.showMessage("Стан практичної змінено на стан 'НА ПАУЗІ'", "normal");
      },
      error: err => {
        this.showError(err);
      }
    });
  }

  doneAction(chapterPart: ChapterPart) {
    this.chaptersService.setPracticeState('READY_TO_REVIEW', chapterPart.id).subscribe({
      next: () => {
        this.updatePractices();
        this.messagesService.showMessage("Стан практичної змінено на стан 'ГОТОВО ДО РЕВЬЮ'", "normal");
      },
      error: err => {
        this.showError(err);
      }
    });
  }

  showError(error: any) {
    console.error("Не можливо оновити статус практичної: " + error);
    this.messagesService.showMessage("Помилка при зміні стану практичної. Див. консоль", "error");
  }

}
