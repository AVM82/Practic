import {Component, Input} from "@angular/core";
import {CommonModule} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {Report} from "../../models/report";
import {Chapter} from "src/app/models/chapter";
import { ReportState } from "src/app/enums/app-constans";
import { ReportService } from "src/app/services/report.service";
import { User } from "src/app/models/user";
import { NewReportDialogComponent } from "../new-report/new-report-dialog.component";

@Component({
    selector: 'report-card',
    standalone: true,
    templateUrl: './report-card.component.html',
    imports: [CommonModule, MatIconModule, MatButtonModule, MatButtonModule,
        MatButtonModule, MatCardModule, MatDialogModule],
    styleUrls: ['/report-card.component.css']
})
export class ReportCardComponent {
    @Input() report!: Report;
    @Input() chapter!: Chapter;
    @Input() chapters!: Chapter[];
    @Input() isStudent!: boolean;
    @Input() isMentor!: boolean;
    @Input() slug!: string;
    @Input() me!: User;
    readonly STATE_ANNOUNCED = ReportState.ANNOUNCED;
    readonly STATE_STARTED = ReportState.STARTED;
    readonly STATE_FINISHED = ReportState.FINISHED;
    readonly STATE_CANCELLED = ReportState.CANCELLED;
    readonly STATE_APPROVED = ReportState.APPROVED;

    constructor(
        public reportService: ReportService,
        public changingReportDialog: MatDialog,
        public cancelingReportDialog: MatDialog
    ) {
    }

    canStart(): boolean {
        return this.isToday(this.report.date) && this.report.state === ReportState.ANNOUNCED
    }

    canStop(): boolean {
        return this.isToday(this.report.date) && this.report.state === ReportState.STARTED
    }

    isToday(reportDate: Date): boolean{
        const date = new Date(reportDate);
        const today = new Date();        
        return date.getFullYear() === today.getFullYear()
                && date.getMonth() === today.getMonth()
                && date.getDate() === today.getDate();
    }

    pressLikeButton() {
        this.reportService.updateReportLikes(this.report)
    }

    openChangeReportDialog(): void {
        const dialogRef = this.changingReportDialog.open(NewReportDialogComponent,
            {
                height: '385px', width: '640px',
                data: {
                    report: this.report,
                    chapter: this.chapter,
                    chapters: this.chapters,
                    slug: this.slug
                },
            });
        dialogRef.afterClosed().subscribe(result => {
            if (result)
                this.reportService.updateReport(this.report, result, this.isStudent, this.me.id, this.chapter);
        });
    }

    openCancelReportDialog(): void {
        const dialogRef = this.cancelingReportDialog.open(NewReportDialogComponent,
            {
                height: '385px', width: '640px',
                data: { report: this.report,
                        chapter: this.chapter,
                        chapters: this.chapters
                }
            });
        dialogRef.afterClosed().subscribe(result => {
            if (result)
                this.reportService.cancelReport(result, this.chapter);
        });
    }
    
}
