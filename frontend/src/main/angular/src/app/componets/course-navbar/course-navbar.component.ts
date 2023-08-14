import {Component, Input} from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import {Course} from "../../models/course/course";
import {MatIconModule} from "@angular/material/icon";
import {Chapter} from "../../models/course/chapter";

@Component({
  selector: 'app-course-navbar',
  standalone: true,
  imports: [CommonModule, NgFor, NgIf, MatIconModule],
  templateUrl: './course-navbar.component.html',
  styleUrls: ['./course-navbar.component.css']
})
export class CourseNavbarComponent {
  @Input() course: Course | undefined;
  @Input() chapters: Chapter[] = [];
}

