import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule  } from '@angular/forms';
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
    imports: [AngularSvgIconModule, ReactiveFormsModule, CommonModule]
  })
export class CreateCourseComponent implements OnInit{
  courses: Course[] = [];
  newCourse?: Course;
  createMethod: CreateMethod = 'Interactive';

  checkoutForm = this.formBuilder.group({
    slug: '',
    shortName: '',
    name: '',
    svg: ''
  });
  
  constructor(
    private coursesService: CoursesService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
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
    if (this.checkoutForm.controls.slug) {
      const slug = this.checkoutForm.value.slug;
      for(let course of this.courses) {
        if (slug === course.slug)
          return this.setRedColor('slug');
      }
    }
    return true;
  }
 
  checkShort(): boolean {
    if (this.checkoutForm.controls.shortName) {
      const shortName = this.checkoutForm.value.shortName;
      for(let course of this.courses) {
        if (shortName === course.shortName)
          return this.setRedColor('shortName');
      }
    }
    return true;
  }
   
  checkName(): boolean {
    const name = this.checkoutForm.value.name ? this.checkoutForm.value.name : '';
    return name.length >= 5 ? true : this.setRedColor('name');
  }

  onSubmit(): void {
    const slug = this.checkSlug();
    const short = this.checkShort();
    const name = this.checkName();
    if (slug && short && name) {
      this.coursesService.postCourse(this.checkoutForm.value.slug, this.checkoutForm.value.shortName,
                                     this.checkoutForm.value.name, this.checkoutForm.value.svg)
      .subscribe(response => {
        console.warn('Course \"%s\" is created', response.name);
        window.location.href = '/courses/' + response.slug;
      })
    } else
      console.error('Confict course data');
  }
}