import { CommonModule } from '@angular/common';
import { Component } from "@angular/core";
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-verification-email-dialog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './verification-email-dialog.component.html',
  styleUrls: ['./verification-email-dialog.component.css']
})
export class VerificationEmailDialogComponent {

  constructor(private dialogRef: MatDialogRef<VerificationEmailDialogComponent>) {
  }

  onClose(): void {
    this.dialogRef.close();
  }
}
