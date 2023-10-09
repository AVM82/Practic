import {Component, Inject,OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormControl, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatSelectModule} from "@angular/material/select";
import {NewStudentReport} from "../../models/newStudentReport/newStudentReport";
import {Chapter} from "../../models/course/chapter";
import {DatePipe, NgForOf} from "@angular/common";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule} from '@angular/material/core';
import {TimeSlot} from "../../models/timeSlot/time-slot";
import * as _moment from 'moment';
import {default as _rollupMoment} from 'moment';
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";
import 'moment/locale/uk';
import {CoursesService} from "../../services/courses/courses.service";
import { BehaviorSubject } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { CommonModule } from '@angular/common';
import { TopicReportService } from '../../services/topic-report.service';
import { Observable } from 'rxjs';


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
        { provide: MAT_DATE_LOCALE, useValue: 'uk' },
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
export class NewReportDialogComponent implements OnInit{
    minDate: Date;
    maxDate: Date;
    date = new FormControl(moment());
    dateStr: string = '';
    openChapters$ = new BehaviorSubject<number[]>([]);
    activeChapter:number = 1;
    topicsReport$: Observable<{ topic: string }[]> = new BehaviorSubject<{ topic: string }[]>([]);;

        constructor(
        public dialogRef: MatDialogRef<NewReportDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public newStudentReport: NewStudentReport,
        @Inject(MAT_DIALOG_DATA) public chaptersList: { chapters: Chapter[] },
        @Inject(MAT_DIALOG_DATA) public timeslotsMap: { timeslots: Map<string, TimeSlot[]> },
        private coursesService: CoursesService, 
        private topicReportService: TopicReportService
        
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
    
        this.openChapters$.subscribe(chapters => {
            if (chapters.length > 0) {
                this.activeChapter = chapters[chapters.length - 1]; 
                this.initTopicsReports();
            }
        });
    }

      updateActiveChapter(selectChapter:number){
        this.activeChapter=selectChapter;
        this.initTopicsReports();
      }

    getOpenChapters(): void {
        
        this.coursesService.getOpenChapters().subscribe({
          next: chapters => {
            const ids = chapters.map(chapter => chapter.id).sort((a, b) => a - b);
            this.openChapters$.next(ids);
            
          },
          error: error => {
            console.error('Помилка при запиті доступних глав', error);
            this.openChapters$.next([]);
          }
        });
      }

    initTopicsReports(){
        console.log(this.
            activeChapter+" new student chapt");
        this.topicReportService.getTopicsReportsOnChapter(this.activeChapter).subscribe({
            next: topics => {
                const topicsReports = topics.map((topic:any) => topic.topic);
               (this.topicsReport$ as BehaviorSubject<{ topic: string }[]>).next(topics); 
            },
            error: error => {
              console.error('Помилка при отриманні доступних тем доповіді', error);
              (this.topicsReport$ as BehaviorSubject<{ topic: string }[]>).next([]); 
            }
          });
    }


    getnewStudentReport():NewStudentReport{
        this.newStudentReport.chapter=this.activeChapter;
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
        return this.timeslotsMap.timeslots[this.dateStr];
    }
    formatTime(timeValue:string): string {
        const parts = timeValue.split(':');
        if (parts.length >= 2) {
            return `${parts[0]}:${parts[1]}`;
        }
        return timeValue;
    }

    protected readonly Date = Date;
}

