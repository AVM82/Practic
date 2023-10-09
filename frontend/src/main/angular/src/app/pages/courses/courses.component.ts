import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses/courses.service";
import {Course} from "../../models/course/course";
import {MatCardModule} from '@angular/material/card';
import {NgForOf} from '@angular/common';
import {RouterLink} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {AngularSvgIconModule, SvgIconRegistryService} from 'angular-svg-icon';

@Component({
    selector: 'app-courses',
    templateUrl: './courses.component.html',
    styleUrls: ['./courses.component.css'],
    standalone: true,
    imports: [NgForOf, MatCardModule, RouterLink, MatIconModule, AngularSvgIconModule]
})
export class CoursesComponent implements OnInit{
  courses: Course[] = [];

  constructor(private coursesService: CoursesService, private svg_registry: SvgIconRegistryService) {
  }

  ngOnInit(): void {
    this.coursesService.getAllCourses().subscribe(courses => {
      if (courses) {
        this.courses = courses;
        courses.forEach(course => this.svg_registry.addSvg(course.slug, course.svg));
      }
    });
  }
}
