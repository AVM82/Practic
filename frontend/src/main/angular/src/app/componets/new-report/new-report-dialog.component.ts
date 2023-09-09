import {Component, Inject, Input} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatSelectModule} from "@angular/material/select";
import {NewStudentReport} from "../../models/newStudentReport/newStudentReport";
import {Chapter} from "../../models/course/chapter";
import {NgForOf} from "@angular/common";
import {MatNativeDateModule} from '@angular/material/core';

@Component({
  selector: 'app-new-report',
  templateUrl: './new-report-dialog.component.html',
  styleUrls: ['./new-report-dialog.component.css'],
  standalone: true,
  imports: [
      MatDialogModule,
    FormsModule,
    MatButtonModule,
    MatSelectModule,
    NgForOf,
    MatFormFieldModule, MatInputModule, MatDatepickerModule, MatNativeDateModule],
})
export class NewReportDialogComponent {

  constructor(
      public dialogRef: MatDialogRef<NewReportDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public newStudentReport: NewStudentReport,
      @Inject(MAT_DIALOG_DATA) public chaptersList:{ chapters: Chapter[] }

  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

}
