import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, RouterOutlet} from "@angular/router";
import {ChapterReportsComponent} from "../../modules/chapter-reports/chapter-reports.component";
import {ReportCardComponent} from "../../componets/report-card/report-card.component";
import {StudentReport} from "../../models/report/studentReport";
import {ReportServiceService} from "../../services/report/report-service.service";
import {MatCardModule} from "@angular/material/card";
import {CoursesService} from "../../services/courses/courses.service";
import {Chapter} from "../../models/chapter/chapter";
import {NewReportDialogComponent} from "../../componets/new-report/new-report-dialog.component";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {TimeSlot} from "../../models/timeSlot/time-slot";
import {TimeSlotService} from "../../services/timeSlot/time-slot.service";
import {Level} from "../../models/level/level";


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
    templateUrl: './report-dashboard.component.html',
    styleUrls: ['./report-dashboard.component.css']
})
export class ReportDashboardComponent implements OnInit {
    reports: StudentReport[][] = [];
    chapters: Chapter[] = [];
    levels: Level[] = []
    timeslots: Map<string, TimeSlot[]> = new Map<string, TimeSlot[]>();

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
            this.updateData(slug)}
        });
    }

    updateData(slug:string):void{
                this.loadLevels(slug);
                this.loadChapters(slug);
                this.loadReports(slug);
                this.loadTimeSlots(slug);
                this.createTimeSlots(slug)
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

    loadLevels(slug: string): void {
        this.coursesService.getLevels(slug).subscribe(levels => {
            this.levels = levels;
            console.log('level inside method subscribe');
            console.log(this.levels)
        });
    }

    loadTimeSlots(slug: string): void {
        this.timeSlotService.getAllAvailableTimeSlots(slug).subscribe(timeslots => {
            this.timeslots = timeslots;
            console.log('timeslots inside method  loadTimeSlots()');
            console.log(this.timeslots);
        });
    }

    createTimeSlots(slug: string): void {
        this.timeSlotService.createNewTimeslots(slug).subscribe();
    }
    openDialog(): void {
        const dialogRef = this.dialog.open(NewReportDialogComponent,
            {
                height: '420px',
                width: '800px',
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
                    this.reportService.createNewReport(result, slug).subscribe(
                        () => {
                        }
                    );

this.timeSlotService.updateTimeslotAvailability(result.timeslotId, slug).subscribe(() => {
                    });                    
                }
            });
            
        });
    }
}