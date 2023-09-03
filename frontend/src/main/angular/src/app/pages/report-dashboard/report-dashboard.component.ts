import {Component,  OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from "@angular/router";
import {ChapterReportsComponent} from "../../modules/chapter-reports/chapter-reports.component";
import {ReportCardComponent} from "../../componets/report-card/report-card.component";
import {StudentReport} from "../../models/report/studentReport";
import {ReportServiceService} from "../../services/report/report-service.service";

@Component({
  selector: 'report-dashboard',
  standalone: true,
  imports: [CommonModule, RouterOutlet, ChapterReportsComponent, ReportCardComponent],
  templateUrl: './report-dashboard.component.html'
})
export class ReportDashboardComponent implements OnInit {
  courseSlug: string = 'java-dev-tools'
  reports: StudentReport[][]=[];
  constructor(
      private reportService: ReportServiceService
  ) {}

  ngOnInit(): void {
    this.loadReports(this.courseSlug);
  }
  loadReports(slug: string): void {
    this.reportService.getAllActualReports(slug).subscribe(reports => {
      this.reports.push(...reports);
      this.reports = [...this.reports];
    });
  }
}