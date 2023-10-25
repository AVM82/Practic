import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {AppConstants} from "../../enums/app-constans";
import {ActivatedRoute} from "@angular/router";
import {TokenStorageService} from "../../services/auth/token-storage.service";
import {AuthService} from "../../services/auth/auth.service";
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormControl, ReactiveFormsModule, FormsModule, Validators } from '@angular/forms';
import { EmailPassAuthService } from 'src/app/services/emailPassAuth/email-pass-auth.service';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule,MatFormFieldModule,FormsModule,MatIconModule,ReactiveFormsModule,MatInputModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  linkedinURL = AppConstants.LINKEDIN_AUTH_URL;
  isLoggedIn = false;
  isLoginFailed = false;
  isRegister = true;
  errorMessage = '';
  currentUser: any;
  hidePassword: boolean = true;
  passwordControl = new FormControl('');
  nameControl = new FormControl('', Validators.required);
  emailControl = new FormControl('', [Validators.required, Validators.email]);

  constructor(
      private route: ActivatedRoute,
      private authService: AuthService,
      private tokenStorage: TokenStorageService,
      private emailAuth: EmailPassAuthService
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

  saveUser(): void {
    
    if (this.emailControl.valid && this.passwordControl.valid) {
        const name: string| null = this.nameControl.value!;
        const email: string| null= this.emailControl.value!;
        const password: string| null = this.passwordControl.value!;

        const username:string = name==null?" ":name;

        if (name!=null||email!=null||password!=null) {
          this.emailAuth.postData(username, email, password).subscribe(
            response => {
              this.tokenStorage.saveToken(response.accessToken);
                this.login(response.user);
                this.nameControl.reset();
                 this.emailControl.reset();
                 this.passwordControl.reset();
            },
            error => {
                console.error("User not added:", error);
            }
        )
    }
        ;
    } else {
        console.error("Invalid data. Name, email, and password are required.");
    }

}  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }

}