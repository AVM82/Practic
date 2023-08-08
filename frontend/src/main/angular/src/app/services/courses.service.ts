import { Injectable } from '@angular/core';
import {Course} from "../models/course";

@Injectable({
  providedIn: 'root'
})
export class CoursesService {
  courses: Course[] = [];

  constructor() {
    this.initializeFakeData();
  }
  getAllCourses(): Course[] {
    return this.courses;
  }

  private initializeFakeData(): void {
    this.courses.push(
        new Course('Angular', 'Web framework'),
        new Course('React', 'JavaScript library'),
        new Course('Vue.js', 'Progressive framework')
    );
  }
}
