import {Component, Input} from "@angular/core";
import {CommonModule, NgFor, NgIf} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";
import {RouterLink} from "@angular/router";
import {ReportServiceService} from "../../services/report/report-service.service";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {ChangingReportDialogComponent} from "../changing-report-dialog/changing-report-dialog.component";
import {CancelingReportDialogComponent} from "../canceling-report-dialog/canceling-report-dialog.component";
import {TimeSlot} from "../../models/timeSlot/time-slot";
import {ReportDashboardComponent} from "../../pages/report-dashboard/report-dashboard.component";

@Component({
    selector: 'report-card',
    standalone: true,
    templateUrl: './report-card.component.html',
    imports: [CommonModule,
        NgFor,
        NgIf,
        MatIconModule,
        MatButtonModule,
        RouterLink,
        MatButtonModule,
        MatButtonModule,
        MatCardModule,
        MatDialogModule,],
    styleUrls: ['/report-card.component.css']
})
export class ReportCardComponent {
    @Input() reportId!: number;
    @Input() reportTopic!: string
    @Input() studentName!: string
    @Input() dateValue!: string
    @Input() timeValue!: string
    @Input() timeslotId!: number
    @Input() profilePictureUrl!: string
    @Input() likedPersonsIdList!: number[];
    @Input() studentId!: number;
    @Input() currentUserId!: number;
    @Input() timeslots!: { timeslots: Map<string, TimeSlot[]> };

    constructor(
        private reportService: ReportServiceService,
        private reportDashboard: ReportDashboardComponent,
        public changingReportDialog: MatDialog,
        public cancelingReportDialog: MatDialog,
    ) {
    }

    formatTime(): string {
        const parts = this.timeValue.split(':');
        if (parts.length >= 2) {
            return `${parts[0]}:${parts[1]}`;
        }
        return this.timeValue;
    }

    pressLikeButton() {
        this.reportService.updateReportLikeList(this.reportId).subscribe(res => {
            this.likedPersonsIdList = res.likedPersonsIdList;
        });

    }

    isCurrentUserReport() {
        return this.studentId == this.currentUserId;
    }

    openChangeReportDialog(): void {
        const dialogRef = this.changingReportDialog.open(ChangingReportDialogComponent,
            {
                height: '60%',
                width: '50%',
                data: {
                    studentReport: {
                        id: this.reportId,
                        title: this.reportTopic,
                        date: this.dateValue,
                        time: this.timeValue,
                        timeslotId: this.timeslotId
                    },
                    timeslots: this.timeslots,
                },
            });


        dialogRef.afterClosed().subscribe(result => {
            console.log("res of changing dialog")
            console.log(result);
            if (result != null ) {
                this.reportService.updateReport(result).subscribe(() => {
                    this.reportDashboard.ngOnInit();// Перенаправление на текущую страницу
                });
            }
        });
    }

    openCancelReportDialog(): void {
        const dialogRef = this.cancelingReportDialog.open(CancelingReportDialogComponent,
            {
                height: '25%',
                width: '30%',
                data: {
                    studentReport: {
                        id: this.reportId,
                        title: this.reportTopic,
                        date: this.dateValue,
                        time: this.timeValue,
                        timeslotId: this.timeslotId
                    },
                    reportId: this.reportId,
                },
            });

        dialogRef.afterClosed().subscribe(result => {
            console.log("res of canceling dialog")
            console.log(result);
            this.reportService.deleteReport(result).subscribe(() => {
                this.reportDashboard.ngOnInit();// Перенаправление на текущую страницу
            });
        });
    }
}
