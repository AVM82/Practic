import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatSelectModule} from "@angular/material/select";
import {NewStudentReport} from "../../models/newStudentReport";
import {AsyncPipe, CommonModule, DatePipe, NgForOf} from "@angular/common";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule} from '@angular/material/core';
import * as _moment from 'moment';
import {default as _rollupMoment} from 'moment';
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";
import 'moment/locale/uk';
import {BehaviorSubject} from 'rxjs';
import {TopicReportService} from '../../services/topic-report.service';
import {Chapter} from "../../models/chapter";
import {TopicReport} from "../../models/report";


const moment = _rollupMoment || _moment;

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
        {
            provide: DateAdapter,
            useClass: MomentDateAdapter,
            deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS],
        },
        {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
        {provide: MAT_DATE_LOCALE, useValue: 'uk'},
        AsyncPipe
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
        MatNativeDateModule,
        DatePipe,
        CommonModule
    ],
})
export class NewReportDialogComponent implements OnInit {
    minDate: Date;
    maxDate: Date;
    date = new FormControl(moment(), Validators.required);
    timeslot = new FormControl('', Validators.required);
    chapter = new FormControl('', Validators.required);
    title = new FormControl('', Validators.compose([
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(100)
    ]));
    dateStr: string = '';
    topicsReport: TopicReport [] = [];
    openChapters$ = new BehaviorSubject<any[]>([]);
    activeChapterId: number = this.data.chapters[this.data.chapters.length - 1].id;

    constructor(
        public dialogRef: MatDialogRef<NewReportDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public newStudentReport: NewStudentReport,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private topicReportService: TopicReportService,
    ) {
        const currentYear = new Date().getFullYear();
        const currentMonth = new Date().getMonth();
        const currentDay = new Date().getDate();
        this.minDate = new Date(currentYear, currentMonth, currentDay);
        this.maxDate = new Date(currentYear, currentMonth, currentDay + 14);
        moment.locale('uk');
    }

    ngOnInit(): void {
        this.getOpenChapters();
        console.log(this.data.chapters)
        if (this.activeChapterId > 0) {
            this.initTopicsReports();
        }
    }

    updateActiveChapter(selectChapter: number) {
        this.activeChapterId = selectChapter;
        this.initTopicsReports();
    }

    getOpenChapters(): void {
        // @ts-ignore
        const ids = this.data.chapters.map((chapter: Chapter) => this.extractChapterInfo(chapter)).sort((a, b) => a - b);
        this.openChapters$.next(ids);
    }

    extractChapterInfo(chapter: Chapter): { num: number; id: number } {
        return {
            num: chapter.number,
            id: chapter.id,
        };
    }


    initTopicsReports() {
        console.log(this.activeChapterId)
        this.topicReportService.getTopicsReportsOnChapter(this.activeChapterId).subscribe({
            next: topics => {
                if (topics) {
                    this.topicsReport = topics;
                } else {
                    console.warn('No topics for this chapter');
                }
            },
            error: error => {
                console.error('Помилка при отриманні доступних тем доповіді', error);
            }
        });
    }


    getNewStudentReport(): NewStudentReport {
        this.newStudentReport.chapterId = this.activeChapterId;
        return this.newStudentReport;
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    getTimeslots(date: any) {
        if (date != null) {
            this.dateStr = date.format('YYYY-MM-DD');
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

    setNewStudentReportTime(selectedTime: string) {
        this.newStudentReport.time = this.formatTime(selectedTime);
    }

    protected readonly Date = Date;
}
