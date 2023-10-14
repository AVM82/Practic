import {Component, Input, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthService} from "../../services/auth/auth.service";
import {TokenStorageService} from "../../services/auth/token-storage.service";
import {ActivatedRoute} from "@angular/router";
import {InfoMessagesService} from "../../services/info-messages.service";
import { CoursesService } from 'src/app/services/courses/courses.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-apply-btn',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './apply-btn.component.html',
  styleUrls: ['./apply-btn.component.css']
})
export class ApplyBtnComponent {
  @Input() showApplyButton: boolean = false;
  @Input() slug: string = '';

  constructor(
      private tokenStorageService:TokenStorageService,
      private authService:AuthService,
      private messagesService: InfoMessagesService
  ) {
  }

  onApplyClick() {
    this.authService.applyOnCourse(this.slug).subscribe({
      next: user => {
        this.tokenStorageService.saveUser(user);
        this.messagesService.showMessage("Заявка прийнята", "normal")
      },
      error: error => {
        console.error('Помилка при відправці заявки', error);
        this.messagesService.showMessage("Помилка відправки заявки", "error")
      }
    });
  }
}
