import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule  } from '@angular/forms';
import { CoursesService } from '../../services/courses/courses.service';
import { Course } from 'src/app/models/course/course';
import {AngularSvgIconModule} from 'angular-svg-icon';
import { CommonModule } from '@angular/common';
import { CreateMethod } from 'src/app/enums/create-method-enum';

@Component({
    selector: 'app-create-course',
    templateUrl: './create-course.component.html',
    styleUrls: ['./create-course.component.css'],
    standalone: true,
    imports: [AngularSvgIconModule, ReactiveFormsModule, CommonModule, FormsModule]
  })
export class CreateCourseComponent implements OnInit{
  courses: Course[] = [];
  newCourse?: Course;
  createMethod: CreateMethod = 'Interactive';
  properties: string = '';
  capability: boolean = false;

  checkoutForm = this.formBuilder.group({
    slug: '',
    name: '',
    svg: ''
  });
  
  constructor(
    private coursesService: CoursesService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.capability = this.coursesService.me.hasAnyRole('ADMIN', 'COLLABORATOR');
    if (this.capability)
      this.coursesService.getAllCourses().subscribe(courses => {
        this.courses = courses;
      })
  }

  setMethod(method: CreateMethod): void {
    this.createMethod = method;
  }

  setBlackColor(id: string): void {
    const elem = document.getElementById(id);
    if  (elem)
      elem.style.setProperty("color", "black");
  }

  setRedColor(id: string): boolean {
    const elem = document.getElementById(id);
    if  (elem)
      elem.style.setProperty("color", "red");
    return false;
  }

  checkSlug(): boolean {
    const slug = this.checkoutForm.value.slug?.trim();
    if (slug) {
      this.checkoutForm.value.slug = slug;
      for(let course of this.courses) {
        if (slug === course.slug)
          return this.setRedColor('slug');
      }
      return true;
    }
    return false;
  }
 
  checkName(): boolean {
    const name = this.checkoutForm.value.name?.trim();
    return name && name.length >= 5 ? true : this.setRedColor('name');
  }

  onSubmit(): void {
    switch(this.createMethod) {
      case 'Interactive': {
        const slug = this.checkSlug();
        const name = this.checkName();
        if (slug && name) {
          this.coursesService.postCourseInteractive(this.checkoutForm.value.slug,
                                        this.checkoutForm.value.name, this.checkoutForm.value.svg)
            .subscribe(response => this.processResponse(response));
        } else
          console.warn('Check fields');
        break;
      }

      case 'FromProperties': {
        const prop = this.properties.trim();
        if (prop) {
          this.coursesService.postCourseProperties(this.properties)
            .subscribe(response => this.processResponse(response));
        } else
          console.warn('Check properties');
        break;
      }

      case 'FromFile': {
        console.warn('This mode is not work yet.');
        break;
      }
    }
  }

  processResponse(course: Course): void {
    if (course) {
      console.warn('Course "%s" is created', course.name);
      window.location.href = '/courses/' + course.slug;
    } else
    console.warn('Confict course data');
  }

}