import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses/courses.service";
import {MatCardModule} from '@angular/material/card';
import {NgForOf, NgIf} from '@angular/common';
import {Router, RouterLink} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {AngularSvgIconModule, SvgIconRegistryService} from 'angular-svg-icon';
import { TokenStorageService } from 'src/app/services/auth/token-storage.service';
import { CourseProp } from 'src/app/models/course/course.prop';
import { Course } from 'src/app/models/course/course';
import { User } from 'src/app/models/user/user';

@Component({
    selector: 'app-courses',
    templateUrl: './courses.component.html',
    styleUrls: ['./courses.component.css'],
    standalone: true,
    imports: [NgForOf, NgIf, MatCardModule, RouterLink, MatIconModule, AngularSvgIconModule]
})
export class CoursesComponent implements OnInit{
  router: Router;
  coursesProp: CourseProp[] = [];
  createCapability: boolean = false;
  me!: User;

  constructor(
    private tokenStorageService: TokenStorageService,
    private coursesService: CoursesService, 
    private router0: Router,
    private svg_registry: SvgIconRegistryService
  ) { 
    this.createCapability = this.coursesService.me.hasAnyRole('ADMIN', 'COLLABORATOR');
    this.router = router0;
    this.me = this.tokenStorageService.getMe();
  }

  ngOnInit(): void {
    this.coursesService.getAllCourses().subscribe(courses => {
      if (courses) {
        courses.forEach(course => {
          this.svg_registry.addSvg(course.slug, course.svg);
          this.coursesProp.push(new CourseProp (
                  course.name,
                  course.slug,
                  this.buildRoute(course.slug)
          ))
        })

      }
    });
  }

  private buildRoute(slug: string): string {
    if (this.me != null ) {
      let number = this.me.getCourseActiveChapterNumber(slug);
      if (number != 0) 
        return slug + '/chapters/' + number;
      if (this.me.isMentor(slug))
        return slug;
    }
    return slug + '/main';
  }

  onSelect(slug: string): void {
    this.coursesService.setCourse(slug);
  }
}
