import {Component, OnInit} from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import {Chapter, Course} from "../../models/course";
import {CoursesService} from "../../services/courses.service";
import {MatIconModule} from "@angular/material/icon";

@Component({
  selector: 'app-course-navbar',
  standalone: true,
  imports: [CommonModule, NgFor, NgIf, MatIconModule],
  templateUrl: './course-navbar.component.html',
  styleUrls: ['./course-navbar.component.css']
})
export class CourseNavbarComponent implements OnInit{
  course!: Course;
  chapters: any;

  constructor(private coursesService: CoursesService) {}

  ngOnInit(): void {
    this.course = this.coursesService.getCourse();
  }

  navigateToChapter(chapter: Chapter): void {
    console.log(chapter);
  }
}

