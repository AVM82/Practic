import {Component, Input, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StudentMetricsService} from "../../services/admin/student-metrics.service";
import {TableWidgetComponent} from "../../componets/table-widget/table-widget.component";
import { RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";

@Component({
  selector: 'app-ready-practic-metric',
  standalone: true,
  imports: [CommonModule, TableWidgetComponent, RouterLink, ReactiveFormsModule, MatIconModule],
  templateUrl: './practic-metric.component.html',
  styleUrls: ['./practic-metric.component.css']
})
export class PracticMetricComponent implements OnInit {
  @Input() shouldShowHeader: boolean = true;

  displayedColumns: string[] = ['personName', 'chapterName', 'state'];
  columnNameConverterMap: { [key: string]: string } = {
    'personName': 'ПІБ',
    'chapterName': 'Розділ',
    'state': 'Стан'
  };

  dataSource: any[] = [];
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
        this.dataSource.push(...students);
        this.dataSource = [...this.dataSource];
      });
    } else {
      this.showState.splice(index, 1);
      this.dataSource = this.dataSource.filter(student => student.state !== state);
    }
  }

  onStateButtonClick(state: string): void {
    this.loadStudentsByState(state);
  }

}
