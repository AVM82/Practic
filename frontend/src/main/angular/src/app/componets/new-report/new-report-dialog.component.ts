import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormControl, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatSelectModule} from "@angular/material/select";
import {NewStudentReport} from "../../models/newStudentReport/newStudentReport";
import {Chapter} from "../../models/course/chapter";
import {NgForOf} from "@angular/common";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule} from '@angular/material/core';
import {TimeSlot} from "../../models/timeSlot/time-slot";

// Depending on whether rollup is used, moment needs to be imported differently.
// Since Moment.js doesn't have a default export, we normally need to import using the `* as`
// syntax. However, rollup creates a synthetic default module and we thus need to import it using
// the `default as` syntax.
// tslint:disable-next-line:no-duplicate-imports
import * as _moment from 'moment';
import {default as _rollupMoment} from 'moment';
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";


const moment = _rollupMoment || _moment;

// See the Moment.js docs for the meaning of these formats:
// https://momentjs.com/docs/#/displaying/format/
export const MY_FORMATS = {
    parse: {
        dateInput: 'DD-MM-YYYY',
    },
    display: {
        dateInput: 'DD MMMM',
        monthYearLabel: 'MMMM ',
        dateA11yLabel: 'LL',
        monthYearA11yLabel: 'MMMM ',
    },
};

@Component({
    selector: 'app-new-report',
    templateUrl: './new-report-dialog.component.html',
    styleUrls: ['./new-report-dialog.component.css'],
    providers: [
        // `MomentDateAdapter` can be automatically provided by importing `MomentDateModule` in your
        // application's root module. We provide it at the component level here, due to limitations of
        // our example generation script.
        {
            provide: DateAdapter,
            useClass: MomentDateAdapter,
            deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS],
        },
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
    ],

    standalone: true,
    imports: [
        MatDialogModule,
        FormsModule,
        MatButtonModule,
        MatSelectModule,
        NgForOf,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatDatepickerModule,
        MatNativeDateModule],
})
export class NewReportDialogComponent {
    minDate: Date;
    maxDate: Date;
    date = new FormControl(moment());
    dateStr: string = '';

    constructor(
        public dialogRef: MatDialogRef<NewReportDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public newStudentReport: NewStudentReport,
        @Inject(MAT_DIALOG_DATA) public chaptersList: { chapters: Chapter[] },
        @Inject(MAT_DIALOG_DATA) public timeslotsMap: { timeslots: Map<String, TimeSlot[]> },
    ) {
        const currentYear = new Date().getFullYear();
        const currentMonth = new Date().getMonth();
        const currentDay = new Date().getDate();
        this.minDate = new Date(currentYear, currentMonth, currentDay);
        this.maxDate = new Date(currentYear, currentMonth, currentDay + 14);
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    getTimeslots(date: any) {
        if (date != null) {
            this.dateStr = date.format('YYYY-MM-DD');
        }
        // @ts-ignore
        return this.timeslotsMap.timeslots[this.dateStr];
    }

    protected readonly Date = Date;
}
