import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses/courses.service";
import {Course} from "../../models/course/course";
import { MatCardModule } from '@angular/material/card';
import { NgForOf } from '@angular/common';
import {RouterLink} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {SlugifyPipe} from "../../pipes/slugify.pipe";

@Component({
    selector: 'app-courses',
    templateUrl: './courses.component.html',
    styleUrls: ['./courses.component.css'],
    standalone: true,
  imports: [NgForOf, MatCardModule, RouterLink, MatIconModule, SlugifyPipe]
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
