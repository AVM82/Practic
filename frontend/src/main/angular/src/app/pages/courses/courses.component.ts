import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses/courses.service";
import {MatCardModule} from '@angular/material/card';
import {NgForOf} from '@angular/common';
import {RouterLink} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {AngularSvgIconModule, SvgIconRegistryService} from 'angular-svg-icon';
import { TokenStorageService } from 'src/app/services/auth/token-storage.service';
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
        const meId = this.coursesService.me.id;
        courses.forEach(course => {
          this.svg_registry.addSvg(course.slug, course.svg);
            let route;
            if (course.mentors.some(mentor => mentor.id == meId))
                route = 'courses/' + course.slug;
            else
              route = (this.tokenStorageService.isStudent(course.slug)
                  ? 'courses/' + course.slug + '/chapters/' + this.coursesService.getActiveChapterNumber(course.slug)
                  : 'courses/' + course.slug + '/main'); 
            this.coursesProp.push(new CourseProp (
              course.name,
              course.slug,
              route
             ))
        })
      }
    });
  }
  
  onSelect(slug: string): void {
    this.coursesService.setCourse(slug);
  }
}
