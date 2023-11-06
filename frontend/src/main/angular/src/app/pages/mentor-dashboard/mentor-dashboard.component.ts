import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterOutlet} from "@angular/router";
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { MentorService } from 'src/app/services/mentor.service';
import { Applicant, CourseApplicants } from 'src/app/models/applicant';
import { MatTableDataSource } from '@angular/material/table';
import { CoursesService } from 'src/app/services/courses.service';
import { Course } from 'src/app/models/course';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-mentor-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mentor-dashboard.component.html',
  styleUrls: ['./mentor-dashboard.component.css']
})
export class MentorDashboardComponent implements OnInit {
  me!: User;
  courses: Course[] = [];
  applicants?: CourseApplicants[];
  mentorService: MentorService;

  constructor(
    private tokenStorage: TokenStorageService,
    private courseService:  CoursesService,
    private mentorService0: MentorService,
    private router: Router
  ) {
    this.me = tokenStorage.getMe();
    if (!this.me.hasAdvancedRole())
      router.navigate(['']);
    this.mentorService = mentorService0;
  }

  ngOnInit(): void {
    this.me.mentors.forEach(mentor => {
      console.log(mentor);
      this.courseService.getCourse(mentor.slug).subscribe(course => {
        console.log(course);
        this.courses.push(course!); 
      });
      this.mentorService.getMyApplicants().subscribe(applicants => this.applicants = applicants);
    })
  }

  getApplicants(slug: string): CourseApplicants {
    const courseApplicants = this.applicants?.find(course => course.slug === slug);
    return courseApplicants ? courseApplicants : {slug: slug, applicants: []};
  }

  operable(applicant: Applicant): boolean {
    return !applicant.isApplied && !applicant.isRejected;
  }

  getState(applicant: Applicant): string {
    if (applicant.isApplied)
      return 'прийнятий';
    if (applicant.isRejected)
      return 'відхилений';
    return 'очікує';
  }

}
