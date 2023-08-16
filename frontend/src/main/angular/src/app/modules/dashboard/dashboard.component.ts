import {Component} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ReadyPracticMetricComponent} from "../ready-practic-metric/ready-practic-metric.component";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ReadyPracticMetricComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

}
