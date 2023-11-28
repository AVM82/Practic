import { CommonModule } from '@angular/common';
import { Component } from "@angular/core";
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";

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
