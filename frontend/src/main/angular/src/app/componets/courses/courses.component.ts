import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses.service";
import {Course} from "../../models/course";
import { MatCardModule } from '@angular/material/card';
import { NgForOf } from '@angular/common';

@Component({
    selector: 'app-courses',
    templateUrl: './courses.component.html',
    styleUrls: ['./courses.component.css'],
    standalone: true,
    imports: [NgForOf, MatCardModule]
})
export class CoursesComponent implements OnInit{
  courses: Course[] = [];

  constructor(private courseService: CoursesService) {
  }

  getAllCourses(){
    this.courses  = this.courseService.getAllCourses();
  }

  ngOnInit(): void {
    this.getAllCourses();
  }
}
