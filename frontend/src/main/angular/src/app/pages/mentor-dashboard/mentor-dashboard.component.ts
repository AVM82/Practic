import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from "@angular/router";
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { MentorService } from 'src/app/services/mentor.service';
import { Applicant, CourseApplicants } from 'src/app/models/applicant';
import { User } from 'src/app/models/user';
import { CourseStudents, Student } from 'src/app/models/student';
import { STATE_FINISHED, STATE_READY_TO_REVIEW } from 'src/app/enums/app-constans';
import { Course } from 'src/app/models/course';
import { MatTooltipModule } from '@angular/material/tooltip';


@Component({
  selector: 'app-mentor-dashboard',
  standalone: true,
  imports: [CommonModule, MatTooltipModule],
  templateUrl: './mentor-dashboard.component.html',
  styleUrls: ['./mentor-dashboard.component.css']
})
export class MentorDashboardComponent implements OnInit {
  me!: User;
  myCourses: Course[] = [];
  courseApplicants: CourseApplicants[] = [];
  courseStudents: CourseStudents[] = [];
  mentorService: MentorService;
  ready: string = STATE_READY_TO_REVIEW;
  finished: string = STATE_FINISHED;

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
    this.mentorService.getMyCourses(this.myCourses);
    this.mentorService.getMyApplicants().subscribe(courseApplicants => {
      courseApplicants.forEach(course =>
        this.courseApplicants.push(new CourseApplicants(course.courseName, this.getApplicants(course.applicants))))
      });
    this.mentorService.getMyStudents().subscribe(courseStudents => {
      courseStudents.forEach(course =>
        this.courseStudents.push(new CourseStudents(course.courseName, course.chapterNumbers, this.getStudents(course.students))))
    })
  }

  private getApplicants(arr: Applicant[]): Applicant[] {
    let result: Applicant[] = [];
    arr.forEach(element => result.push(new Applicant(element)));
    return result;
  }

  private getStudents(arr: Student[]): Student[] {
    let result: Student[] = [];
    arr.forEach(element => result.push(new Student(element)));
    return result;
  }

}
