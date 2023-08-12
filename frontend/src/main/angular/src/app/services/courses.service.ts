import { Injectable } from '@angular/core';
import {Course, sampleCourse} from "../models/course";

@Injectable({
  providedIn: 'root'
})
export class CoursesService {
  courses: Course[] = [];

  getAllCourses(): Course[] {
    return this.courses;
  }

  getCourse(): Course {
    return sampleCourse;
  }
}
