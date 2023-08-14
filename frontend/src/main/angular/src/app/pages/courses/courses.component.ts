import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses/courses.service";
import {Course} from "../../models/course/course";
import { MatCardModule } from '@angular/material/card';
import { NgForOf } from '@angular/common';
import {RouterLink} from "@angular/router";

@Component({
    selector: 'app-courses',
    templateUrl: './courses.component.html',
    styleUrls: ['./courses.component.css'],
    standalone: true,
  imports: [NgForOf, MatCardModule, RouterLink]
})
export class CoursesComponent implements OnInit{
  courses: Course[] = [];

  constructor(private coursesService: CoursesService) {
  }

  ngOnInit(): void {
    this.coursesService.getAllCourses().subscribe(courses => {
      this.courses = courses;
    });
  }
}
