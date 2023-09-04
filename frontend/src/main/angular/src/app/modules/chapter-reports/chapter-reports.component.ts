import {Component, Input} from '@angular/core';
import {CommonModule} from '@angular/common';
import { RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {ReportCardComponent} from "../../componets/report-card/report-card.component";
import {StudentReport} from "../../models/report/studentReport";
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'chapter-reports',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, ReportCardComponent, MatButtonModule, MatIconModule],
  templateUrl: './chapter-reports.component.html',
  styleUrls: ['./chapter-reports.component.css']
})
export class ChapterReportsComponent  {
  @Input() shouldShowHeader: boolean = true;
  @Input() chapterNumber!: number
  @Input() studentReports!: StudentReport[];
  startOfRange: number = 0;
  endOfRange: number = 3;


  showReports(){
    const sortList = this.studentReports
    .sort((a,b) => {
      return new Date(a.dateTime).getTime() - new Date(b.dateTime).getTime();
    });
    return sortList.slice(this.startOfRange,this.endOfRange);
  }
  nextReports(){
    if(this.endOfRange<this.studentReports.length) {
      this.startOfRange += 1;
      this.endOfRange += 1;
    }
  }
  previousReports(){
    if(this.startOfRange>0) {
      this.startOfRange -= 1;
      this.endOfRange -= 1;
    }
  }
  isReportsLengthMore(){
    return this.studentReports.length>3;
  }


}

