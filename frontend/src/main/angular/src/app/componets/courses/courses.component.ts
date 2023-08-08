import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses.service";
import {Course} from "../../models/course";

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
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
