import {Component, Input} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {ReportCardComponent} from "../../componets/report-card/report-card.component";
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {Level} from "../../models/level";
import { Chapter } from 'src/app/models/chapter';
import {ReportService} from 'src/app/services/report.service';
import { CoursesService } from 'src/app/services/courses.service';
import { User } from 'src/app/models/user';

@Component({
  selector: 'chapter-reports',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, ReportCardComponent, MatButtonModule, MatIconModule],
  templateUrl: './chapter-reports.component.html',
  styleUrls: ['./chapter-reports.component.css']
})
export class ChapterReportsComponent {
  @Input() levels!:Level[];
  @Input() chapter!: Chapter;
  @Input() chapters!: Chapter[];
  @Input() slug!: string;
  @Input() me!: User;
  @Input() isStudent!: boolean;
  @Input() isMentor!: boolean;

  reportsOnPage: number = 5;
  startOfRange: number = 0;
  endOfRange: number = this.reportsOnPage;

  constructor (
    public courseService: CoursesService,
    public reportService: ReportService
  ) { 
  }

  nextReports() {
    if (this.endOfRange < this.chapter.reports.length) {
      this.startOfRange += 1;
      this.endOfRange += 1;
    }
  }

  previousReports() {
    if (this.startOfRange > 0) {
      this.startOfRange -= 1;
      this.endOfRange -= 1;
    }
  }

  onChange() {
    console.log('chapter-report onchange')
  }
}
