import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule  } from '@angular/forms';
import { CoursesService } from '../../services/courses/courses.service';
import { Course } from 'src/app/models/course/course';
import {AngularSvgIconModule} from 'angular-svg-icon';
import { CommonModule } from '@angular/common';
import { CreateMethod } from 'src/app/enums/create-method-enum';
import { TokenStorageService } from 'src/app/services/auth/token-storage.service';
import { User } from 'src/app/models/user/user';

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
  me: User;

  checkoutForm = this.formBuilder.group({
    slug: '',
    name: '',
    svg: ''
  });
  
  constructor(
    private coursesService: CoursesService,
    private tokenService: TokenStorageService,
    private formBuilder: FormBuilder
  ) {
    this.me = tokenService.getMe();
  }

  ngOnInit(): void {
    this.capability = this.me.hasAnyRole('ADMIN', 'COLLABORATOR');
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
      return !this.courses.some(course => course.slug === slug);
    }
    return false;
  }
 
  checkName(): boolean {
    const name = this.checkoutForm.value.name?.trim();
    return (name && name.length >= 5) || this.setRedColor('name');
  }

  onSubmit(): void {
    switch(this.createMethod) {
      case 'Interactive': {
        const slug = this.checkSlug();
        const name = this.checkName();
        if (slug && name) {
          const svg = this.checkoutForm.value.svg ? this.checkoutForm.value.svg : '';
          this.coursesService.postCourseInteractive(this.checkoutForm.value.slug!,
                                        this.checkoutForm.value.name!, svg)
            .subscribe({
              next: response => {
              console.log(' posted new course : ' , response);
              this.processResponse(response)
              },
            error: () =>
              console.error('new course is not posted')
          });
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