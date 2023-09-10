import {Component, Input, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {TableWidgetComponent} from "../../componets/table-widget/table-widget.component";
import {StudentMetricsService} from "../../services/admin/student-metrics.service";
import {CoursesService} from "../../services/courses/courses.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-inactive-person',
  standalone: true,
  imports: [CommonModule, TableWidgetComponent],
  templateUrl: './inactive-person.component.html',
  styleUrls: ['./inactive-person.component.css']
})
export class InactivePersonComponent implements OnInit{
  @Input() shouldShowHeader: boolean = true;
  isRequestInProgress: boolean = false;

  displayedColumns: string[] = ['name', 'profilePictureUrl', 'btnApply'];
  columnNameConverterMap: { [key: string]: string } = {
    'name': 'ПІБ',
    'profilePictureUrl': 'Фото',
    'btnApply': 'Прийняти заявку'
  };

  data: any[] = [];

  constructor(
      private studentMetricService: StudentMetricsService,
      private coursesService: CoursesService,
      private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
   this.updateData();
  }

  handleAction(event: any) {
    const element = event.element;
    console.log("Element="+JSON.stringify(element));
    console.log(element.applyCourse);
    this.isRequestInProgress = true;
    this.coursesService.confirmApplyOnCourse(element.applyCourse, element.id).subscribe({
      next: value => {
        this.isRequestInProgress = false;
        this.updateData();
      },
      error: err => {
        this.isRequestInProgress = false;
      }
    });
  }

  updateData() {
    this.studentMetricService.getApplicants().subscribe(applicants => {
      this.data = applicants;
    });
  }

}
