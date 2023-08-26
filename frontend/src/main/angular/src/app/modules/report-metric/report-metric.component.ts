import {Component, Input, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StudentMetricsService} from "../../services/admin/student-metrics.service";
import {TableWidgetComponent} from "../../componets/table-widget/table-widget.component";
import { RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";
import {ReportButtonComponent} from "../../componets/report-button/report-button.component";
import {ReportCardComponent} from "../../componets/report-card/report-card.component";


@Component({
  selector: 'app-report-metric',
  standalone: true,
  imports: [CommonModule, TableWidgetComponent, RouterLink, ReactiveFormsModule, MatIconModule, ReportButtonComponent, ReportCardComponent],
  templateUrl: './report-metric.component.html',
  styleUrls: ['./report-metric.component.css']
})
export class ReportMetricComponent implements OnInit {
  @Input() shouldShowHeader: boolean = true;

  displayedColumns: string[] = ['personName', 'chapterName', 'state', 'updatedAt'];
  daysOfWeek: string[] = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  columnNameConverterMap: { [key: string]: string } = {
    'Mon': 'Понеділок',
    'Tue': 'Вівторок',
    'Wed': 'Середа',
    'Thu': 'Четвер',
    'Fri': 'П\'ятниця',
    'Sat': 'Субота',
    'Sun': 'Неділя'
  };

  data: any[] = [];
  practicStates: any[] = [];
  defaultState: string = 'ready_to_review';

  showState: string[] = [];

  constructor(
      private studentMetricService: StudentMetricsService
  ) {}

  ngOnInit(): void {
    this.studentMetricService.getPracticeStates().subscribe(states =>{
      this.practicStates = states;
    });

    this.loadStudentsByState(this.defaultState);
  }

  loadStudentsByState(state: string): void {
    const index = this.showState.indexOf(state);

    if (index === -1) {
      this.showState.push(state);
      this.studentMetricService.getAllPracticesByState(state).subscribe(students => {
        this.data.push(...students);
        this.data = [...this.data];
      });
    } else {
      this.showState.splice(index, 1);
      this.data = this.data.filter(student => student.state !== state);
    }
  }

  onStateButtonClick(state: string): void {
    this.loadStudentsByState(state);
  }

}
