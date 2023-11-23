import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router} from "@angular/router";
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { MentorService } from 'src/app/services/mentor.service';
import { Applicant, CourseApplicants } from 'src/app/models/applicant';
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
  courseApplicants: CourseApplicants[] = [];
  mentorService: MentorService;

  constructor(
    private tokenStorage: TokenStorageService,
    private mentorService0: MentorService,
    private router: Router
  ) {
    this.me = tokenStorage.getMe();
    if (!this.me.hasAdvancedRole())
      router.navigate(['']);
    this.mentorService = mentorService0;
  }

  ngOnInit(): void {
    this.mentorService.getMyApplicants().subscribe(courseApplicants => {
      courseApplicants.forEach(course =>
        this.courseApplicants.push(new CourseApplicants(course.courseName, this.newApplicants(course.applicants))))
      })
  }

  private newApplicants(arr: Applicant[]): Applicant[] {
    let result: Applicant[] = [];
    arr.forEach(element => result.push(new Applicant(element)));
    return result;
  }


  
}
