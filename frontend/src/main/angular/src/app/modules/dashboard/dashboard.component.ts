import {Component} from '@angular/core';
import { CommonModule } from '@angular/common';
import {PracticMetricComponent} from "../practic-metric/practic-metric.component";
import {RouterLink} from "@angular/router";
import {InactivePersonComponent} from "../inactive-person/inactive-person.component";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, PracticMetricComponent, RouterLink, InactivePersonComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent {

}