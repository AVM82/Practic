import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, RouterOutlet} from "@angular/router";
import {ChapterReportsComponent} from "../../modules/chapter-reports/chapter-reports.component";
import {ReportCardComponent} from "../../componets/report-card/report-card.component";
import {ReportServiceService} from "../../services/report-service.service";
import {MatCardModule} from "@angular/material/card";
import {CoursesService} from "../../services/courses.service";
import {NewReportDialogComponent} from "../../componets/new-report/new-report-dialog.component";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {TimeSlot} from "../../models/time-slot";
import {TimeSlotService} from "../../services//time-slot.service";
import {Level} from "../../models/level";
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {MatButtonModule} from "@angular/material/button";
import {ShortChapter} from 'src/app/models/chapter';
import {CalendarEventService} from "../../services/calendar-event.service";
import * as _moment from "moment/moment";
import {default as _rollupMoment} from "moment/moment";
import { TokenStorageService } from 'src/app/services/token-storage.service';

const moment = _rollupMoment || _moment;

@Component({
    selector: 'report-dashboard',
    standalone: true,
    imports: [
        MatDialogModule,
        CommonModule,
        RouterOutlet,
        ChapterReportsComponent,
        ReportCardComponent,
        MatCardModule,
        CourseNavbarComponent,
        MatButtonModule,
    ],
    templateUrl: './report-dashboard.component.html',
    styleUrls: ['./report-dashboard.component.css']
})
export class ReportDashboardComponent implements OnInit/*, OnDestroy*/ {
    chapters: ShortChapter[] = [];
    levels: Level[] = []
    timeslots!: Map<string, TimeSlot[]>;
    currentUserId!: any;

    constructor(
        public dialog: MatDialog,
        private coursesService: CoursesService,
        private tokenStorageService: TokenStorageService,
        private route: ActivatedRoute,
        public reportService: ReportServiceService,
        private timeSlotService: TimeSlotService,
        private calendarEventService: CalendarEventService
    ) {
    }


    ngOnInit(): void {
        this.currentUserId = this.tokenStorageService.getMe().id;
        this.route.paramMap.subscribe(params => {
            const slug = params.get('slug');
            console.log(slug)
            if (slug)
                this.updateData(slug)
        });
    }

    updateData(slug: string): void {
        this.loadLevels(slug);
        this.loadReports(slug);
        this.loadTimeSlots(slug);
        this.createTimeSlots(slug)
    }

    getChapters(chapters: ShortChapter[]) {
        this.chapters = chapters;
    }


    loadReports(slug: string): void {
        this.reportService.getAllActualReports(slug).subscribe(() => {
            this.loadTimeSlots(slug);
        });
    }

    loadLevels(slug: string): void {
        this.coursesService.getLevels(slug).subscribe(levels => {
            this.levels = levels;
            console.log(this.levels)
        });
    }

    loadTimeSlots(slug: string): void {
        this.timeSlotService.getAllAvailableTimeSlots(slug).subscribe(timeslots => {
            this.timeslots = new Map<string, TimeSlot[]>();
            this.timeslots = timeslots;
            console.log(this.timeslots);
        });
    }

    createTimeSlots(slug: string): void {
        this.timeSlotService.createNewTimeslots(slug).subscribe();
    }

    openDialog(): void {
        const dialogRef = this.dialog.open(NewReportDialogComponent,
            {
                height: '60%',
                width: '50%',
                data: {
                    chapters: this.chapters,
                    timeslots: this.timeslots
                },
            });

        dialogRef.afterClosed().subscribe(result => {
            this.route.paramMap.subscribe(params => {
                const slug = params.get('slug');
                console.log('The dialog was closed');
                console.log("result of creating report dialog: ", result);
                if (result.timeslotId && result.title && result.chapter && slug) {
                    this.reportService.createNewReport(result, slug).subscribe();
                    const startReportDateTime = this.toUnionDate(result.date, result.time);
                    console.log("start report date and time: ", startReportDateTime)
                    const endReportDateTime = moment(startReportDateTime).add(30, 'm').toDate();
                    console.log("end report date and time: ", endReportDateTime)
                      this.calendarEventService.sendEmailNotification({
                          "startEvent": startReportDateTime,
                          "endEvent": endReportDateTime,
                          "subjectReport": result.title,
                          "description": "description"
                      }).subscribe()
                }
            });
        });
    }

    toUnionDate(date: any, time: string): Date {
        const parsedDateTime = new Date(date);
        const [hours, minutes] = time.split(':').map(Number);
        parsedDateTime.setHours(hours);
        parsedDateTime.setMinutes(minutes);
        console.log(parsedDateTime)
        return parsedDateTime;
    }
}