import {Component, Input, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthService} from "../../services/auth/auth.service";
import {TokenStorageService} from "../../services/auth/token-storage.service";
import {InfoMessagesService} from "../../services/info-messages.service";
import { CoursesService } from 'src/app/services/courses/courses.service';

@Component({
  selector: 'app-apply-btn',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './apply-btn.component.html',
  styleUrls: ['./apply-btn.component.css']
})
export class ApplyBtnComponent implements OnInit {
  @Input() slug: string = '';
  buttonDisabled: boolean = false;

  constructor(
      private courseService: CoursesService,
      private tokenStorageService: TokenStorageService,
      private authService: AuthService,
      private messagesService: InfoMessagesService
  ) {
  }

  ngOnInit() {
    if (!this.courseService.isStudent) {
      this.courseService.amIwaitingForApply(this.slug).subscribe(waiting =>
        this.buttonDisabled = waiting);
    } 
  }

  onApplyClick() {
    if (confirm("Ви дійсно хочете записатися на цей курс?"))
    this.authService.applyOnCourse(this.slug).subscribe({
      next: user => {
        this.tokenStorageService.saveUser(user);
        this.messagesService.showMessage("Заявка прийнята", "normal");
        document.getElementById('apply')?.setAttribute('disables', 'true');
        this.buttonDisabled = true;
      },
      error: error => {
        console.error('Помилка при відправці заявки', error);
        this.messagesService.showMessage("Помилка відправки заявки", "error")
      }
    });
  }
}
