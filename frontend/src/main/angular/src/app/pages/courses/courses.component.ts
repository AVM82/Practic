import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses/courses.service";
import {MatCardModule} from '@angular/material/card';
import {NgForOf} from '@angular/common';
import {RouterLink} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {AngularSvgIconModule, SvgIconRegistryService} from 'angular-svg-icon';
import { TokenStorageService } from 'src/app/services/auth/token-storage.service';
import { ApiUrls, getActiveChapterNumber, getCourseUrl, getDescriptionUrl } from 'src/app/enums/api-urls';
import { CourseProp } from 'src/app/models/course/course.prop';

@Component({
    selector: 'app-courses',
    templateUrl: './courses.component.html',
    styleUrls: ['./courses.component.css'],
    standalone: true,
    imports: [NgForOf, MatCardModule, RouterLink, MatIconModule, AngularSvgIconModule]
})
export class CoursesComponent implements OnInit{
  coursesProp: CourseProp[] = [];

  constructor(
    private tokenStorageService: TokenStorageService,
    private coursesService: CoursesService, 
    private svg_registry: SvgIconRegistryService
  ) { }

  ngOnInit(): void {
    this.coursesService.getAllCourses().subscribe(courses => {
      if (courses) {
        courses.forEach(course => {
          this.svg_registry.addSvg(course.slug, course.svg);
          this.coursesProp.push(new CourseProp (
            course.name,
            course.slug,
            this.getRoute(course.slug) ));
        });
      }
    });
  }

  getRoute(slug: string): string {
    if (this.tokenStorageService.isMentor(slug, true))
      return getCourseUrl(slug);
    else
      if (this.tokenStorageService.isStudent(slug))
        return getActiveChapterNumber(slug);
      else
        return getDescriptionUrl(slug);
  }
  
}
