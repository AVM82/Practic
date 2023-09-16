import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, RouterOutlet} from "@angular/router";
import {ChapterReportsComponent} from "../../modules/chapter-reports/chapter-reports.component";
import {ReportCardComponent} from "../../componets/report-card/report-card.component";
import {StudentReport} from "../../models/report/studentReport";
import {ReportServiceService} from "../../services/report/report-service.service";
import {MatCardModule} from "@angular/material/card";
import {CoursesService} from "../../services/courses/courses.service";
import {Chapter} from "../../models/course/chapter";
import {NewReportDialogComponent} from "../../componets/new-report/new-report-dialog.component";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {TimeSlot} from "../../models/timeSlot/time-slot";
import {TimeSlotService} from "../../services/timeSlot/time-slot.service";


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
    ],
    templateUrl: './report-dashboard.component.html'
})
export class ReportDashboardComponent implements OnInit {
    reports: StudentReport[][] = [];
    chapters: Chapter[] = [];
    timeslots: Map<String, TimeSlot[]> = new Map<String, TimeSlot[]>();

    constructor(
        public dialog: MatDialog,
        private coursesService: CoursesService,
        private route: ActivatedRoute,
        private reportService: ReportServiceService,
        private timeSlotService: TimeSlotService,
    ) {
    }


    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            const slug = params.get('slug');
            console.log(slug)
            if (slug) {
                this.loadReports(slug);
                this.loadChapters(slug);
                this.loadTimeSlots()
            }
        });
    }

    loadReports(slug: string): void {
        this.reportService.getAllActualReports(slug).subscribe(reports => {
            this.reports.push(...reports);
            this.reports = [...this.reports];
        });
    }

    loadChapters(slug: string): void {
        this.coursesService.getChapters(slug).subscribe(chapters => {
            this.chapters = chapters;
            console.log('chapters inside method subscribe');
            console.log(this.chapters)
        });
    }

    loadTimeSlots(): void {
        this.timeSlotService.getAllAvailableTimeSlots().subscribe(timeslots => {
            this.timeslots = timeslots;
            console.log('timeslots inside method  loadTimeSlots()');
            console.log(this.timeslots);
        });
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
                if (result != null && slug) {
                    this.reportService.createNewReport(result, slug).subscribe();
                    this.timeSlotService.updateTimeslotAvailability(result.timeslotId).subscribe();
                }
            });
            this.ngOnInit();
        });
    }
}