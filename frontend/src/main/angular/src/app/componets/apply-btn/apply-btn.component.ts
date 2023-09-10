import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthService, User} from "../../services/auth/auth.service";
import {TokenStorageService} from "../../services/auth/token-storage.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-apply-btn',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './apply-btn.component.html',
  styleUrls: ['./apply-btn.component.css']
})
export class ApplyBtnComponent implements OnInit {
  isApply: boolean = false;
  applyCourse: string = '';
  courseSlug: string = '';
  constructor(
      private tokenStorageService:TokenStorageService,
      private authService:AuthService,
      private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug');
      if(slug) {
        this.courseSlug = slug;
      }
    });
    const token = this.tokenStorageService.getToken();
    if (token) {
      const user: User = this.tokenStorageService.getUser();
      this.applyCourse = user.applyCourse;
    }
  }

  onApplyClick() {
    this.authService.applyOnCourse().subscribe({
      next: () => {
        console.log('Заявка прийнята');
        this.isApply = true;
        this.applyCourse = this.courseSlug;
      },
      error: error => {
        console.error('Помилка при відправці заявки', error);
      }
    });
  }
}
