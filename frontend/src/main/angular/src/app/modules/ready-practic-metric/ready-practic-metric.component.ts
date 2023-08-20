import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {StudentMetricsService} from "../../services/admin/student-metrics.service";
import {TableWidgetComponent} from "../../componets/table-widget/table-widget.component";

@Component({
  selector: 'app-ready-practic-metric',
  standalone: true,
  imports: [CommonModule, TableWidgetComponent],
  templateUrl: './ready-practic-metric.component.html',
  styleUrls: ['./ready-practic-metric.component.css']
})
export class ReadyPracticMetricComponent implements OnInit{
  displayedColumns: string[] = ['id', 'name', 'chapterId'];
  dataSource: any[]=[];

  columnMap: { [key: string]: string } = {
    'id': 'Id',
    'name': 'ПІБ',
    'chapterId': 'Розділ'
  };

  constructor(private studentMetricService: StudentMetricsService) {
  }
  ngOnInit(): void {
    this.studentMetricService.getStudents().subscribe(students =>{
      this.dataSource = students;
    })
  }

}
