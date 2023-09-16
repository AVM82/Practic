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
        const token = this.tokenStorageService.getToken();
        if (token) {
          const user: User = this.tokenStorageService.getUser();
          if (user && user.hasApplyOnCourse(slug)) {
            this.isApply = true;
          }
        }
      }
    });

  }

  onApplyClick() {
    this.authService.applyOnCourse(this.courseSlug).subscribe({
      next: user => {
        this.tokenStorageService.saveUser(user);
        this.isApply = true;
      },
      error: error => {
        console.error('Помилка при відправці заявки', error);
      }
    });
  }
}
