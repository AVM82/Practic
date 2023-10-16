import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatSelectModule} from "@angular/material/select";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";

@Component({
    selector: 'app-canceling-report-dialog',
    templateUrl: './canceling-report-dialog.component.html',
    styleUrls: ['./canceling-report-dialog.component.css'],
    standalone: true,
    imports: [
        MatDialogModule,
        FormsModule,
        MatButtonModule,
        MatSelectModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatDatepickerModule,
        MatNativeDateModule,
    ],

})
export class CancelingReportDialogComponent {
    constructor(
        public dialogRef: MatDialogRef<CancelingReportDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: { reportId: number }) {
    }
    onNoClick(): void {
        this.dialogRef.close();
    }

}