import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses/courses.service";
import {MatCardModule} from '@angular/material/card';
import {NgForOf, NgIf} from '@angular/common';
import {RouterLink} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {AngularSvgIconModule, SvgIconRegistryService} from 'angular-svg-icon';
import { TokenStorageService } from 'src/app/services/auth/token-storage.service';
import { CourseProp } from 'src/app/models/course/course.prop';
import { User } from 'src/app/models/user/user';


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
  otherCourse = 'grey';
  applicantCourse = 'purple';
  me!: User;

  constructor(
    private tokenStorageService: TokenStorageService,
    private coursesService: CoursesService, 
    private svg_registry: SvgIconRegistryService
  ) { 
    this.me = this.tokenStorageService.getMe();
    this.createCapability = this.me.hasAnyRole('ADMIN', 'COLLABORATOR');
  }

  ngOnInit(): void {
    if (this.me != null )
      this.coursesService.getAllCourses().subscribe(courses => {
        if (courses)
          courses.forEach(course => {
            this.svg_registry.addSvg(course.slug, course.svg);
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
      });
  }

  onSelect(slug: string): void {
    this.coursesService.setCourse(slug);
  }

}
