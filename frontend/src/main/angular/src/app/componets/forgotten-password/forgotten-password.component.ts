import { CommonModule } from '@angular/common';
import { Component } from "@angular/core";
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { TokenStorageService } from "../../services/token-storage.service";
import { User } from 'src/app/models/user';
import { EmailPassAuthService } from 'src/app/services/email-pass-auth.service';
import {
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from "@angular/forms";

@Component({
  standalone: true,
  imports: [CommonModule, MatFormFieldModule, FormsModule, ReactiveFormsModule,
    MatDialogModule, MatInputModule, MatIconModule],
  selector: 'app-fargot-pass-dialog',
  templateUrl: './forgotten-password.component.html',
  styleUrls: ['./forgotten-password.component.css']
})

export class FargotPassDialogComponent {
  emailControl = new FormControl("", [Validators.required, Validators.email]);
  secretCode = new FormControl("", [Validators.required]);
  newPassFirst = new FormControl("", [Validators.required]);
  newPassSecond = new FormControl("", [Validators.required]);
  isEmailSend = false;
  isCodeSend = false;
  hidePasswordFirst: boolean = true;
  hidePasswordSecond: boolean = true;
  isPassMath: boolean = true;
  email: string = ""
  isEmailIncorect: boolean = false;
  isPersonNotFound: boolean = false;

  constructor(private dialogRef: MatDialogRef<FargotPassDialogComponent>, private tokenStorageService: TokenStorageService,
    private emailAuthService: EmailPassAuthService) {
  }

  ngOnInit(): void {
    const token = this.tokenStorageService.getToken();
    if (token) {
      const user: User = this.tokenStorageService.getUser();
      this.email = user.email;

    }
  }




  checkPasswordMismatch(): boolean {
    return this.newPassFirst.value == this.newPassSecond.value;
  }

  onClose(): void {
    this.dialogRef.close();
  }

  sendMail() {
    if (this.emailControl.value && this.emailControl.valid) {
      this.emailAuthService.sendEmailForGetSecretCode(this.emailControl.value).subscribe(
        (response) => {
          this.isPersonNotFound = false;
          this.isEmailIncorect = false;
          this.isEmailSend = true;
        }, (error) => {
          this.isPersonNotFound = true;
          this.isEmailIncorect = true;
        }
      );
    }
  }


  isValidsecretCode(): boolean {
    return this.secretCode.hasError('required');
  }

  getPersonNotFound(): boolean {
    return this.isPersonNotFound;
  }

  sendCode() {
    if (this.emailControl.value && this.emailControl.valid && this.secretCode.value) {
      this.emailAuthService.sendSecretCodeForPasswordReset(this.secretCode.value, this.emailControl.value).subscribe(
        (response) => {
          this.isEmailIncorect = false;
          this.isCodeSend = true;
        }, (error) => {
          this.secretCode.hasError('required');
        }
      );
    }
  }


  sendPassword() {

    if (this.emailControl.value && this.emailControl.valid && this.secretCode.value && this.newPassSecond.value && this.newPassSecond.valid) {
      this.emailAuthService.sendNewPassword(this.secretCode.value, this.emailControl.value, this.newPassSecond.value).subscribe(
        (response) => {
          this.isEmailIncorect = false;
          this.dialogRef.close();
        }, (error) => {
          this.secretCode.hasError('required');
        }
      );
    }
    this.dialogRef.close();

  }
  togglePasswordVisibilityFirst(): void {
    this.hidePasswordFirst = !this.hidePasswordFirst;
  }
  togglePasswordVisibilitySecond(): void {
    this.hidePasswordSecond = !this.hidePasswordSecond;
  }
}