import {Component, Input} from '@angular/core';
import {CommonModule} from '@angular/common';
import { RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {ReportCardComponent} from "../../componets/report-card/report-card.component";
import {StudentReport} from "../../models/report/studentReport";


@Component({
  selector: 'chapter-reports',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, ReportCardComponent],
  templateUrl: './chapter-reports.component.html',
  styleUrls: ['./chapter-reports.component.css']
})
export class ChapterReportsComponent  {
  @Input() shouldShowHeader: boolean = true;
  @Input() chapterNumber!: number
  @Input() studentReports!: StudentReport[];


}
