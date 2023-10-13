import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatSelectModule} from "@angular/material/select";
import {DatePipe, NgForOf} from "@angular/common";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule} from "@angular/material/core";
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";
import {MY_FORMATS} from "../new-report/new-report-dialog.component";
import * as _moment from 'moment';
import {default as _rollupMoment} from 'moment';
import 'moment/locale/uk';

const moment = _rollupMoment || _moment;

/*export const MY_FORMATS = {
  parse: {
    dateInput: 'DD-MM-YYYY',
  },
  display: {
    dateInput: 'DD MMMM',
    monthYearLabel: 'MMMM ',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM ',
  },
};*/

@Component({
    selector: 'app-changing-report-dialog',
    templateUrl: './changing-report-dialog.component.html',
    styleUrls: ['./changing-report-dialog.component.css'],
    providers: [
        {
            provide: DateAdapter,
            useClass: MomentDateAdapter,
            deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS],
        },
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
        {provide: MAT_DATE_LOCALE, useValue: 'uk'}
    ],
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
        NgForOf,
        DatePipe
    ],
})
export class ChangingReportDialogComponent {


    constructor(
        public dialogRef: MatDialogRef<ChangingReportDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
    ) {
        const currentYear = new Date().getFullYear();
        const currentMonth = new Date().getMonth();
        const currentDay = new Date().getDate();
        this.minDate = new Date(currentYear, currentMonth, currentDay);
        this.maxDate = new Date(currentYear, currentMonth, currentDay + 14);
        moment.locale('uk');
    }

    minDate: Date;
    maxDate: Date;
    dateCF = new FormControl(moment(), Validators.required);
    timeslotCF = new FormControl('', Validators.required);
    titleCF = new FormControl('', Validators.compose([
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(100)
    ]));
    dateStr: string = '';


    onNoClick(): void {
        console.log('on No Click method')
        this.dialogRef.close();
    }

    getTimeslots(date: any) {

        if (date != null && date.format) {
            this.dateStr = date.format('YYYY-MM-DD');
            // @ts-ignore
            return this.data.timeslots[this.dateStr];
        }
        if (date != null) {
            this.dateStr = date;
        }
        // @ts-ignore
        return this.data.timeslots[this.dateStr];
    }

    formatTime(timeValue: string): string {

        const parts = timeValue.split(':');
        if (parts.length >= 2) {
            return `${parts[0]}:${parts[1]}`;
        }
        return timeValue;
    }

    protected readonly Date = Date;
}
