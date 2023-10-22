import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { FeedbackService } from 'src/app/services/feedback/feedbacks.service';
import {TokenStorageService} from "../../services/auth/token-storage.service";
import { MatInputModule } from '@angular/material/input';
import { User } from 'src/app/models/user/user';

@Component({
  selector: 'app-feedback-dialog',
  standalone: true,
  imports: [CommonModule, MatFormFieldModule, FormsModule, HttpClientModule, MatInputModule],
  templateUrl: './feedback-dialog.component.html',
  styleUrls: ['./feedback-dialog.component.css'],
})
export class FeedbackDialogComponent implements OnInit {
  feedbackText = "";
  email:string=""
  @Output() feedbackSaved = new EventEmitter<any>();


  constructor(private dialogRef: MatDialogRef<FeedbackDialogComponent>,
    private feedbackService: FeedbackService,
    private tokenStorageService:TokenStorageService) { }

    ngOnInit(): void {
      const token = this.tokenStorageService.getToken();
      if (token) {
        const user: User = this.tokenStorageService.getUser();
        this.email = user.email;
      }
    }

  onSave(): void {
    this.feedbackService.postData(this.email, this.feedbackText).subscribe(response=>{
      console.log("Feedback added:",response);
    },error=>{console.error("Feedback nat added:",error);
    })
    this.dialogRef.close(this.feedbackText);
    this.feedbackSaved.emit(this.feedbackText);

  }

  onClose(): void {
    this.dialogRef.close();
  }

}
