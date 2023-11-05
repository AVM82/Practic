import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {CdkAccordionModule} from '@angular/cdk/accordion';
import {MatTooltipModule} from "@angular/material/tooltip";
import {InfoMessagesService} from "../../services/info-messages.service";
import {EditBtnComponent} from 'src/app/componets/edit-btn/edit-course.component';
import {MatChipsModule} from "@angular/material/chips";
import {PracticeStatePipe} from "../../pipes/practice-state.pipe";
import {PracticeButtonsVisibilityPipe} from "../../pipes/practice-btn-visibility.pipe";
import { Chapter } from 'src/app/models/chapter';
import { Practice } from 'src/app/models/practice';
import { User } from 'src/app/models/user';
import { CoursesService } from 'src/app/services/courses.service';
import { StudentService } from 'src/app/services/student.service';
import { ChaptersService } from 'src/app/services/chapters.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { ChapterPart } from 'src/app/models/chapterpart';

@Component({
  selector: 'app-chapter-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink, MatIconModule, EditBtnComponent,
    CdkAccordionModule, MatTooltipModule, MatChipsModule, PracticeStatePipe, PracticeButtonsVisibilityPipe],
  templateUrl: './chapter-details.component.html',
  styleUrls: ['./chapter-details.component.css']
})
export class ChapterDetailsComponent implements OnInit {
  practices: Practice[] = [];
  isStudent: boolean = false;
  chapter?: Chapter;
  me!: User;

  constructor(
    private coursesService: CoursesService,
    private studentService: StudentService,
    private chaptersService: ChaptersService,
      private route: ActivatedRoute,
      private messagesService: InfoMessagesService,
      private tokenStorageService: TokenStorageService,
  ) {
    this.me = this.tokenStorageService.getMe();
  }

  ngOnInit(): void {
    const url = this.route.snapshot.url.map(urlSegment => urlSegment.path);
    const slug = url[1];
    const number = url.length >= 4 ? +url[3] : 0;
    if (slug && number > 0) {
        if (this.me.isStudent(slug)) {
  //          this.updatePractices();
              this.isStudent = true;
          }
        this.coursesService.getChapter(slug, number).subscribe(chapter =>
            this.chapter = chapter);
    } else
      history.back();
  }

  showPartNumber() {
    return this.chapter && this.chapter.parts.length > 1;
  }

  setPractices() {
    const practices = this.tokenStorageService.getPractice();
    if(practices){
      this.practices = practices;
    } else {
      this.updatePractices();
    }
  }

  updatePractices() {
    this.chaptersService.getMyPractices().subscribe({
      next: value => {
        this.practices = value;
        this.tokenStorageService.updatePractice(value);
      }
    })
  }

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
