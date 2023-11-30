import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses.service";
import {MatCardModule} from '@angular/material/card';
import {NgForOf, NgIf} from '@angular/common';
import {RouterLink} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {AngularSvgIconModule} from 'angular-svg-icon';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { User } from 'src/app/models/user';
import { ROLE_ADMIN, ROLE_STAFF } from 'src/app/enums/app-constans';
import { Course } from 'src/app/models/course';


@Component({
    selector: 'app-courses',
    templateUrl: './courses.component.html',
    styleUrls: ['./courses.component.css'],
    standalone: true,
    imports: [MatCardModule, RouterLink, MatIconModule, AngularSvgIconModule]
})
export class CoursesComponent implements OnInit{
  courses: Course[] = [];
  createCapability: boolean = false;
  me!: User;

  constructor(
    private tokenStorageService: TokenStorageService,
    private coursesService: CoursesService
  ) { 
    this.me = this.tokenStorageService.getMe();
    this.createCapability = this.me.hasAnyRole(ROLE_ADMIN, ROLE_STAFF);
  }

  ngOnInit(): void {
      this.coursesService.getAllCourses().subscribe(courses => this.courses = courses);
  }

  onSelect(slug: string): void {
    this.coursesService.setCourse(slug);
  }

  getRoute(slug: string): string {
    if (this.me.isStudent(slug)) {
      let number = this.me.getCourseActiveChapterNumber(slug);
      return slug + (number != 0 ? '/chapters/' + number : '');
    }
    return this.me.isMentor(slug) ? slug : slug + '/main';
  }

}
