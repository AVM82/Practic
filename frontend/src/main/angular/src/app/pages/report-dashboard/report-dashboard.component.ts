import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ChapterReportsComponent} from "../../componets/chapter-reports/chapter-reports.component";
import {ReportCardComponent} from "../../componets/report-card/report-card.component";
import {MatCardModule} from "@angular/material/card";
import {CoursesService} from "../../services/courses.service";
import {NewReportDialogComponent} from "../../componets/new-report/new-report-dialog.component";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {Level} from "../../models/level";
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {MatButtonModule} from "@angular/material/button";
import {Chapter} from 'src/app/models/chapter';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import {User} from "../../models/user";
import { ReportService } from 'src/app/services/report.service';

@Component({
    selector: 'report-dashboard',
    standalone: true,
    imports: [ MatDialogModule, CommonModule, ChapterReportsComponent, ReportCardComponent,
        MatCardModule, CourseNavbarComponent, MatButtonModule ],
    templateUrl: './report-dashboard.component.html',
    styleUrls: ['./report-dashboard.component.css']
})
export class ReportDashboardComponent {
    chapters: Chapter[] = [];
    openedChapters: Chapter[] = [];
    levels: Level[] = []
    me!:User;
    slug: string = '';
    isStudent: boolean = false;
    isMentor: boolean = false;

    constructor(
        public dialog: MatDialog,
        private coursesService: CoursesService,
        private tokenStorageService: TokenStorageService,
        public reportService: ReportService
    ) {
        this.me = tokenStorageService.getMe();
    }

    getSlug(slug: string) {
        this.slug = slug;
        this.coursesService.getLevels(slug).subscribe(levels => this.levels = levels);
        this.isMentor = this.me.isMentor(slug);
        this.isStudent = this.me.isStudent(slug);
    }

    getChapters(chapters: Chapter[]) {
        this.chapters = chapters;
        this.openedChapters = chapters.filter(chapter => !chapter.hidden);
        this.openedChapters.forEach(chapter => ReportService.refreshMyReports(chapter));
    }

    openDialog(): void {
        const dialogRef = this.dialog.open(NewReportDialogComponent,
            {
                height: '50%', width: '60%',
                data: { chapters: this.openedChapters,
                        slug: this.slug }
            });
        dialogRef.afterClosed().subscribe(result => {
            console.log("result of creating report dialog: ", result);
            if (result)
                this.reportService.createReport(result, this.isStudent, this.me.id, this.slug);
        });
    }

}
