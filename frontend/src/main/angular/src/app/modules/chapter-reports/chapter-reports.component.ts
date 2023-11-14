import {Component, Input} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {ReportCardComponent} from "../../componets/report-card/report-card.component";
import {StudentReport} from "../../models/report";
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {Level} from "../../models/level";

@Component({
  selector: 'chapter-reports',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, ReportCardComponent, MatButtonModule, MatIconModule],
  templateUrl: './chapter-reports.component.html',
  styleUrls: ['./chapter-reports.component.css']
})
export class ChapterReportsComponent {
  @Input() shouldShowHeader: boolean = true;
  @Input() levels!:Level[];
  @Input() chapterNumber!: number
  @Input() studentReports!: StudentReport[];
  @Input() currentUserId!:number;
  @Input() timeslots!: any;

  reportsNumberOnPage: number = 5;
  startOfRange: number = 0;
  endOfRange: number = this.reportsNumberOnPage;
  chapterColor: string = '#6565A3';


  showReports() {
    const sortedList = this.studentReports;
    sortedList.sort((a, b) => {
      return new Date(a.date).getTime() - new Date(b.date).getTime();
    });
    return sortedList.slice(this.startOfRange, this.endOfRange);
  }

  nextReports() {
    if (this.endOfRange < this.studentReports.length) {
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

  isReportsLengthMore() {
    return this.studentReports.length > this.reportsNumberOnPage;
  }

  getChapterColor(chapter:number):any{
    for(const element of this.levels) {
      if (element.number == 1 && element.chapterN.includes(chapter)) {
        this.chapterColor = '#84C984';
      }
      if (element.number == 2 && element.chapterN.includes(chapter)) {
        this.chapterColor = '#D86D6D';
      }
      if (element.number == 3 && element.chapterN.includes(chapter)) {
        this.chapterColor =  '#CED069';
      }
    }
    return this.chapterColor;
  }
}