import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses.service";
import {MatCardModule} from '@angular/material/card';
import {NgForOf, NgIf} from '@angular/common';
import {RouterLink} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {AngularSvgIconModule} from 'angular-svg-icon';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { CourseProp } from 'src/app/models/course.prop';
import { User } from 'src/app/models/user';
import { ROLE_ADMIN, ROLE_COLLABORATOR } from 'src/app/enums/app-constans';


@Component({
    selector: 'app-courses',
    templateUrl: './courses.component.html',
    styleUrls: ['./courses.component.css'],
    standalone: true,
    imports: [NgForOf, NgIf, MatCardModule, RouterLink, MatIconModule, AngularSvgIconModule]
})
export class CoursesComponent implements OnInit{
  coursesProp: CourseProp[] = [];
  createCapability: boolean = false;
  studentCourse = 'green';
  mentorCourse = 'blue';
  otherCourse = 'smokey';
  applicantCourse = 'purple';
  me!: User;

  constructor(
    private tokenStorageService: TokenStorageService,
    private coursesService: CoursesService
  ) { 
    this.me = this.tokenStorageService.getMe();
    this.createCapability = this.me.hasAnyRole(ROLE_ADMIN, ROLE_COLLABORATOR);
  }

  ngOnInit(): void {
      this.coursesService.getAllCourses().subscribe(courses => {
          courses.forEach(course => {
                  let route;
                  let color;
                  if (this.me.isStudent(course.slug)) {
                      color = this.studentCourse;
                      let number = this.me.getCourseActiveChapterNumber(course.slug);
                      route = course.slug + (number != 0 ? '/chapters/' + number : '');
                  }
                  if (this.me.isMentor(course.slug)) {
                      color = this.mentorCourse;
                      route = course.slug;
                  } else {
                      route = course.slug + '/main';
                      color = this.me.isApplicant(course.slug) ? this.applicantCourse : this.otherCourse;
                  }
                  this.coursesProp.push(new CourseProp (
                      course.name,
                      course.slug,
                      route,
                      color
                  ))
              })
          this.coursesProp.sort(function(a, b) {
              let x = a.color[0];
              let y = b.color[0];
              if (x < y)
                return -1;
              return x === y ? 0 : 1;
          })
      });
  }

  onSelect(slug: string): void {
    this.coursesService.setCourse(slug);
  }

}
