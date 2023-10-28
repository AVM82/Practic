import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {AppConstants} from "../../enums/app-constans";
import {ActivatedRoute} from "@angular/router";
import {TokenStorageService} from "../../services/auth/token-storage.service";
import {AuthService} from "../../services/auth/auth.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  linkedinURL = AppConstants.LINKEDIN_AUTH_URL;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  currentUser: any;
  constructor(
      private route: ActivatedRoute,
      private tokenStorage: TokenStorageService,
      private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    const token: string | null = this.route.snapshot.queryParamMap.get('token');
    const error: string | null = this.route.snapshot.queryParamMap.get('error');
    const logout: string | null = this.route.snapshot.queryParamMap.get('logout');

    if(logout === 'true') {
      this.tokenStorage.signOut();
      window.location.href = '/login';
    }

    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.currentUser = this.tokenStorage.getUser();
    }
    else if(token) {
      this.tokenStorage.saveToken(token);
      this.authService.getCurrentUser().subscribe({
        next: value => {
          this.login(value);
        },
        error: err => {
          this.errorMessage = err;
          this.isLoginFailed = true;
        }
      })
    }
    else if(error){
      this.errorMessage = error;
      this.redirect();
    }
  }


  login(data: any): void {
    console.log('login data - ',data);
    this.tokenStorage.saveUser(data);
    this.isLoginFailed = false;
    this.isLoggedIn = true;
    this.currentUser = this.tokenStorage.getUser();
    this.redirect();
  }

  redirect(): void {
    const baseUrl = window.location.origin;
    window.history.replaceState({}, document.title, baseUrl);
    window.location.reload();
  }
}
