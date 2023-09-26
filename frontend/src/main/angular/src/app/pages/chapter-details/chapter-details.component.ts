import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {Chapter} from "../../models/chapter/chapter";
import {ChaptersService} from "../../services/chapters/chapters.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {CdkAccordionModule} from '@angular/cdk/accordion';
import {MatTooltipModule} from "@angular/material/tooltip";
import {ChapterPart} from "../../models/chapter/chapterpart";
import {InfoMessagesService} from "../../services/info-messages.service";
import {EditBtnComponent} from 'src/app/componets/edit-btn/edit-course.component';
import {MatChipsModule} from "@angular/material/chips";
import {Practice} from "../../models/practice/practice";
import {TokenStorageService} from "../../services/auth/token-storage.service";
import {PracticeStatePipe} from "../../pipes/practice-state.pipe";
import {PracticeButtonsVisibilityPipe} from "../../pipes/practice-btn-visibility.pipe";

@Component({
  selector: 'app-chapter-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink, MatIconModule, EditBtnComponent,
    CdkAccordionModule, MatTooltipModule, MatChipsModule, PracticeStatePipe, PracticeButtonsVisibilityPipe],
  templateUrl: './chapter-details.component.html',
  styleUrls: ['./chapter-details.component.css']
})
export class ChapterDetailsComponent implements OnInit {
    chapter?: Chapter ;
    showPartNumber: boolean = false;
    practices: Practice[] = [];

  constructor(
      private chaptersService: ChaptersService,
      private route: ActivatedRoute,
      private messagesService: InfoMessagesService,
      private tokenStorageService: TokenStorageService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug');
      const chapterN =  Number(params.get('chapterN'));

      if(slug && chapterN) {
          this.updatePractices();
          this.chaptersService.getChapter(slug, chapterN).subscribe(chapter =>
        {
          this.chapter = chapter;
          this.showPartNumber = chapter.parts.length > 1;
        });
      }
    })
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
