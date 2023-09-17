import {Component, Input, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StudentMetricsService} from "../../services/admin/student-metrics.service";
import {TableWidgetComponent} from "../../componets/table-widget/table-widget.component";
import { RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";
import {CoursesService} from "../../services/courses/courses.service";
import {InfoMessagesService} from "../../services/info-messages.service";

@Component({
  selector: 'app-practic-metric',
  standalone: true,
  imports: [CommonModule, TableWidgetComponent, RouterLink, ReactiveFormsModule, MatIconModule],
  templateUrl: './practic-metric.component.html',
  styleUrls: ['./practic-metric.component.css']
})
export class PracticMetricComponent implements OnInit {
  @Input() shouldShowHeader: boolean = true;
  isRequestInProgress: boolean = false;

  displayedColumns: string[] = ['personName', 'chapterName', 'state', 'updatedAt', 'btnApply'];
  columnNameConverterMap: { [key: string]: string } = {
    'personName': 'ПІБ',
    'chapterName': 'Розділ',
    'state': 'Стан',
    'updatedAt': 'Останнє оновлення',
    'btnApply': 'Прийняти'
  };

  data: any[] = [];
  practicStates: any[] = [];
  defaultState: string = 'ready_to_review';

  showState: string[] = [];

  constructor(
      private studentMetricService: StudentMetricsService,
      private coursesService: CoursesService,
      private messagesService: InfoMessagesService
  ) {}

  ngOnInit(): void {
    this.studentMetricService.getPracticeStates().subscribe(states =>{
      this.practicStates = states;
    });

    this.loadStudentsByState(this.defaultState);
  }

  loadStudentsByState(state: string): void {
    const index = this.showState.indexOf(state.toLowerCase());

    if (index === -1) {
      this.showState.push(state);
      this.studentMetricService.getAllPracticesByState(state).subscribe(students => {
        if(students?.length > 0) {
          this.data.push(...students);
          this.data = [...this.data];
        }

      });
    } else {
      this.showState.splice(index, 1);
      this.data = this.data.filter(student => student.state.toLowerCase() !== state);
    }
  }

  onStateButtonClick(state: string): void {
    this.loadStudentsByState(state);
  }

  handleAction(event: any) {
    const element = event.element;
    this.isRequestInProgress = true;
    console.log("Element = " + JSON.stringify(element));
    this.coursesService.approvePractice(element.studentId, element.chapterPartId).subscribe({
      next: () => {
        this.messagesService.showMessage("Практична підтверджена.", "normal");
        this.isRequestInProgress = false;
        this.loadStudentsByState(this.defaultState);
      },
      error: err => {
        console.error("Error approving practice: " + JSON.stringify(err));
        this.messagesService.showMessage("Помилка при підтверджені", "error");
        this.isRequestInProgress = false;
      }
    })
  }

}
