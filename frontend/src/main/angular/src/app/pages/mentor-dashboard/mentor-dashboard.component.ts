import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-mentor-dashboard',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './mentor-dashboard.component.html'
})
export class MentorDashboardComponent {

}
