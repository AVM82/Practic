import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterOutlet} from "@angular/router";
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { MentorService } from 'src/app/services/mentor.service';
import { CourseApplicants } from 'src/app/models/applicant';
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
  courseApplicants?: CourseApplicants[];

  constructor(
    private tokenStorage: TokenStorageService,
    private courseService:  CoursesService,
    private mentorService: MentorService,
    private router: Router
  ) {
    this.me = tokenStorage.getMe();
    if (!this.me.hasAdvancedRole())
      router.navigate(['']);
  }

  ngOnInit(): void {
    this.me.mentors.forEach(mentor => {
      console.log(mentor);
      this.courseService.getCourse(mentor.slug).subscribe(course => {
        console.log(course);
        this.courses.push(course!); 
      });
      this.mentorService.getAllApplicants().subscribe(applicants => this.courseApplicants = applicants);
    })
  }

  getApplicants(slug: string): CourseApplicants {
    const courseApplicants = this.courseApplicants?.find(course => course.slug === slug);
    return courseApplicants ? courseApplicants : {slug: slug, applicants: []};
  }

  apply(event: any) {
    event.target.disable = true;
    this.mentorService.admitApplicant(event.target.id).subscribe({
      next: stateStudent => {
          event.target.innerHTML = stateStudent.registered;
      },
      error: error =>{
        event.target.innerHTML = 'відхилено'
      }
    })
  }
}
