import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatSelectModule} from "@angular/material/select";
import {CommonModule} from "@angular/common";
import {MatNativeDateModule} from '@angular/material/core';
import {FormReport, TopicReport} from "../../models/report";
import { DAYS_AHEAD_REPORT_ANNOUNCE } from 'src/app/enums/app-constans';
import { CalendarEventService } from 'src/app/services/calendar-event.service';
import { ReportService } from 'src/app/services/report.service';


@Component({
    selector: 'app-new-report',
    templateUrl: './new-report-dialog.component.html',
    styleUrls: ['./new-report-dialog.component.css'],
    standalone: true,
    imports: [ MatDialogModule, FormsModule, MatButtonModule, MatSelectModule, MatFormFieldModule, MatInputModule,
        MatDatepickerModule, MatNativeDateModule, CommonModule, ReactiveFormsModule ],
})
export class NewReportDialogComponent implements OnInit {
    minDate: Date;
    minMs: number = 0;
    maxDate: Date;
    dateForm = new FormControl({value: this.data.report?.date || '', disabled: !this.data.slug},
                        Validators.required);
    chapterForm = new FormControl({value: this.data.chapter || this.data.chapters[this.data.chapters.length - 1], 
                                   disabled: !this.data.slug},
                        Validators.required);
    topicForm = new FormControl({value: this.chapterForm.value.topicReports.find((topic: { id: any; }) => topic.id === this.data.report?.topic.id),
                                 disabled: !this.data.slug},
                        Validators.compose([
                                    Validators.required,
                                    Validators.minLength(5),
                                    Validators.maxLength(100)
    ]));
    freeDate: boolean[] = [];
    dateFilter = (d: Date | null): boolean => {
        if (d != null && this.reportDateMs == CalendarEventService.toMs(d))
            return true;
        const index = d == null ? -1 : Math.trunc(d.valueOf() - this.minMs) / 86400000;
        return index >= 0 && index < this.freeDate.length && this.freeDate[index];
    };
    reportDateMs: number = 0;

    constructor(
        private reportService: ReportService,
        public dialogRef: MatDialogRef<NewReportDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        this.minDate = CalendarEventService.dateFromNowPlus(1);
        console.log('minDate=', this.minDate)
        this.minMs = CalendarEventService.toMs(this.minDate);
        this.maxDate = CalendarEventService.dateFromNowPlus(DAYS_AHEAD_REPORT_ANNOUNCE);
        if (data.report) {
            this.reportDateMs = CalendarEventService.toMs(new Date(data.report.date));
            console.log(data.chapter)
        }
    }

    ngOnInit(): void {
        if (this.data.slug)
            this.reportService.fillFreeDateFrom(this.data.slug, this.freeDate, CalendarEventService.toLocalDate(this.minDate));
    }

    getFormReport(): FormReport {
        return {
            id: this.data.report?.id | 0,
            chapter: this.chapterForm.value,
            date: CalendarEventService.stringDateToLocalDate(this.dateForm.value),
            topic: this.topicForm.value!
        };
    }

    correctDate(strdate: string | null): Date | undefined{
        return strdate ? CalendarEventService.toLocalDate(new Date(strdate)) : undefined;
    }

}
