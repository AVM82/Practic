import {Component, EventEmitter, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatDialogRef} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {FeedbackService} from 'src/app/services/feedbacks.service';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from "@angular/material/button";

@Component({
    selector: 'app-feedback-dialog',
    standalone: true,
    imports: [CommonModule, MatFormFieldModule, FormsModule, HttpClientModule, MatInputModule, MatButtonModule],
    templateUrl: './feedback-dialog.component.html',
    styleUrls: ['./feedback-dialog.component.css'],
})
export class FeedbackDialogComponent  {
    feedbackText = "";

    @Output() feedbackSaved = new EventEmitter<any>();


    constructor(private dialogRef: MatDialogRef<FeedbackDialogComponent>,
                private feedbackService: FeedbackService) {
    }

    onSave(): void {
        if (this.feedbackText.trim().length >= 5) {
            this.feedbackService.postData(this.feedbackText).subscribe({
                next: (response) =>
                    this.feedbackSaved.emit(response.feedback),
                error: (error) => {
                    console.error("Feedback not added:", error);
                }
            })
            this.dialogRef.close(this.feedbackText);
        }
    }

    onClose(): void {
        this.dialogRef.close();
    }

}
