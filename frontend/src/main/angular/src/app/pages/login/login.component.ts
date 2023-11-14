import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AppConstants, TOKEN_KEY } from "../../enums/app-constans";
import { ActivatedRoute } from "@angular/router";
import { TokenStorageService } from "../../services/token-storage.service";
import { MatFormFieldModule } from "@angular/material/form-field";
import {
  FormControl,
  ReactiveFormsModule,
  FormsModule,
  Validators,
} from "@angular/forms";
import { EmailPassAuthService } from "src/app/services/email-pass-auth.service";
import { FargotPassDialogComponent } from "src/app/componets/forgotten-password/forgotten-password.component";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatDialogModule, MatDialog } from "@angular/material/dialog";

@Component({
  selector: "app-login",
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    FormsModule,
    MatIconModule,
    ReactiveFormsModule,
    MatInputModule,
    MatDialogModule
  ],
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.css"],
})
export class LoginComponent implements OnInit {
  isRegister = true;
  linkedinURL = AppConstants.LINKEDIN_AUTH_URL;
  hidePassword: boolean = true;
  passwordControl = new FormControl("");
  nameControl = new FormControl("", Validators.required);
  emailControl = new FormControl("", [Validators.required, Validators.email]);

  constructor(
    private route: ActivatedRoute,
    private tokenStorage: TokenStorageService,
    private emailAuth: EmailPassAuthService,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    const token: string | null = this.route.snapshot.queryParamMap.get("token");
    const error: string | null = this.route.snapshot.queryParamMap.get("error");
    const logout: string | null = this.route.snapshot.queryParamMap.get("logout");

    if (logout === "true") {
      this.tokenStorage.signOut();
      window.location.href = "/login";
    }

    if (this.tokenStorage.getToken()) {
      if (!this.tokenStorage.getUser())
        window.sessionStorage.removeItem(TOKEN_KEY);
      this.redirect();
    } else if (token) {
      this.tokenStorage.saveToken(token);
      this.tokenStorage.getCurrentUser().subscribe(
        value => this.login(value));
    } else if (error) 
      this.redirect();
  }

  login(data: any): void {
    this.tokenStorage.saveUser(data);
    this.tokenStorage.me = data;
    this.redirect();
  }

  redirect(): void {
    const baseUrl = window.location.origin;
    window.history.replaceState({}, document.title, baseUrl);
    window.location.reload();
  }

  saveUser(): void {
    if (this.emailControl.valid && this.passwordControl.valid) {
      const name: string | null = this.nameControl.value!;
      const email: string | null = this.emailControl.value!;
      const password: string | null = this.passwordControl.value!;

      const username: string = name == null ? " " : name;

      if (name != null || email != null || password != null) {
        this.emailAuth.postData(username, email, password).subscribe(
          response => {
            this.tokenStorage.saveToken(response.accessToken);
            this.login(response.user);
            this.nameControl.reset();
            this.emailControl.reset();
            this.passwordControl.reset();
          },
          (error) => {
            console.error("User not added:", error);
          }
        );
      }
    } else {
      console.error("Invalid data. Name, email, and password are required.");
    }
  }
  
  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }
  
  forgotPassword(): void {
    const dialogRef = this.dialog.open(FargotPassDialogComponent, {
      width: '600px', 
      height: '300px'
    });  
  }

}