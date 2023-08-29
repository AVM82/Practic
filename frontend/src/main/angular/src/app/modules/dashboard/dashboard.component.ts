import {Component} from '@angular/core';
import { CommonModule } from '@angular/common';
import {PracticMetricComponent} from "../practic-metric/practic-metric.component";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, PracticMetricComponent, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent {

}