import {Component, OnInit} from '@angular/core';
import {CoursesService} from "../../services/courses/courses.service";
import {MatCardModule} from '@angular/material/card';
import {NgForOf, NgIf} from '@angular/common';
import {Router, RouterLink} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {AngularSvgIconModule, SvgIconRegistryService} from 'angular-svg-icon';
import { TokenStorageService } from 'src/app/services/auth/token-storage.service';
import { CourseProp } from 'src/app/models/course/course.prop';

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

  constructor(
    private tokenStorageService: TokenStorageService,
    private coursesService: CoursesService, 
    private router0: Router,
    private svg_registry: SvgIconRegistryService
  ) { 
    this.createCapability = this.coursesService.me.hasAnyRole('ADMIN', 'COLLABORATOR');
    this.router = router0;
  }

  ngOnInit(): void {
    this.coursesService.getAllCourses().subscribe(courses => {
      if (courses) {
        const meId = this.coursesService.me.id;
        courses.forEach(course => {
          this.svg_registry.addSvg(course.slug, course.svg);
            let route;
            if (course.mentors.some(mentor => mentor.id == meId))
                route = course.slug;
            else
              route = (this.tokenStorageService.isStudent(course.slug)
                  ? course.slug + '/chapters/' + this.coursesService.getActiveChapterNumber(course.slug)
                  : course.slug + '/main'); 
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
