import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {AppConstants, TOKEN_KEY} from "../../enums/app-constans";
import {ActivatedRoute} from "@angular/router";
import {TokenStorageService} from "../../services/token-storage.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  linkedinURL = AppConstants.LINKEDIN_AUTH_URL;
//  isLoggedIn = false;
//  isLoginFailed = false;
//  errorMessage = '';
//  currentUser: any;

  constructor(
      private route: ActivatedRoute,
      private tokenStorage: TokenStorageService
  ) {}

  ngOnInit(): void {
    const token: string | null = this.route.snapshot.queryParamMap.get('token');
    const error: string | null = this.route.snapshot.queryParamMap.get('error');
    const logout: string | null = this.route.snapshot.queryParamMap.get('logout\\');

    if(logout === 'true') {
      this.tokenStorage.signOut();
      window.location.href = '/login';
    }

    if (this.tokenStorage.getToken()) {
//      this.isLoggedIn = true;
//      this.currentUser = this.tokenStorage.getUser();
      if (!this.tokenStorage.getUser())
        window.sessionStorage.removeItem(TOKEN_KEY);
      this.redirect();
    }
    else if(token) {
      this.tokenStorage.saveToken(token);
      this.tokenStorage.getCurrentUser().subscribe({
        next: value => {
          this.login(value);
        },
        error: err => {
 //         this.errorMessage = err;
 //         this.isLoginFailed = true;
        }
      })
    }
    else if(error){
//      this.errorMessage = error;
      this.redirect();
    }
  }


  login(data: any): void {
    this.tokenStorage.saveUser(data);
  //  this.isLoginFailed = false;
  //  this.isLoggedIn = true;
  //  this.currentUser = this.tokenStorage.getUser();
    this.tokenStorage.me = data;
    this.redirect();
  }

  redirect(): void {
    const baseUrl = window.location.origin;
    window.history.replaceState({}, document.title, baseUrl);
    window.location.reload();
  }
}
