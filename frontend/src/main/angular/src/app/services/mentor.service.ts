import { Injectable } from "@angular/core";
import { Applicant, CourseApplicants } from "../models/applicant";
import { Observable } from "rxjs/internal/Observable";
import { HttpClient } from "@angular/common/http";
import { TokenStorageService } from "./token-storage.service";
import { ApiUrls, addMentorUrl, removeMentorUrl } from "../enums/api-urls";
import { StateStudent } from "../models/student";
import { User } from "../models/user";
import { Course } from "../models/course";
import { Mentor, StateMentor } from "../models/mentor";
import { CoursesService } from "./courses.service";

@Injectable({
    providedIn: 'root'
})
export class MentorService {
  
    constructor(
        private tokenStorageService: TokenStorageService,
        private coursesService: CoursesService,
        private http: HttpClient
    ) {}

    getMyApplicants(): Observable<CourseApplicants[]> {
        return this.http.get<CourseApplicants[]>(ApiUrls.Applicants);
    }

    getCourseApplicants(slug: string): Observable<CourseApplicants> {
        return this.http.get<CourseApplicants>(ApiUrls.Applicants + `/` + slug);
    }

    admit(applicant: Applicant): void {
        this.http.post<StateStudent>(ApiUrls.Applicants + `/admit/` + applicant.id, {}).subscribe(student => {
            applicant.applied = true;
            applicant.student = student;
            if (this.tokenStorageService.me!.maybeNewStudent(applicant)) {
                this.tokenStorageService.saveMe();
                this.coursesService.clearCourse(applicant.student.slug);
            }
        });
    }

    reject(applicant: Applicant): void {
        console.log(applicant);
        this.http.post<Applicant>(ApiUrls.Applicants + `/reject/` + applicant.id, {}).subscribe(rejectedApplicant => {
            applicant.update(rejectedApplicant);
            if (this.tokenStorageService.me!.maybeNotApplicant(rejectedApplicant))
                this.tokenStorageService.saveMe();
        });
    }
    
    addMentor(course: Course, user: User): void {
        this.http.post<Mentor>(addMentorUrl(course.slug, user.id), {}).subscribe(mentor => {
            course.mentors.push(mentor);
            let state: StateMentor = {mentorId: mentor.id, inactive: mentor.inactive, slug: mentor.slug};
            user.addMentor(state);
            if (user.id == this.tokenStorageService.me!.id) {
                this.coursesService.clearCourse(course.slug);
                this.tokenStorageService.saveMe();
            }
        });
    }

    removeMentor(course: Course, user: User): void {
        const mentorId = user.getMentor(course.slug)!.mentorId;
        this.http.post<boolean>(removeMentorUrl(mentorId), {}).subscribe(success => {
            if (success) {
                course.mentors = course.mentors.filter(mentor => mentor.id != mentorId);
                user.removeMentorById(mentorId);
                if (user.id == this.tokenStorageService.me!.id) {
                    this.coursesService.clearCourse(course.slug);
                    this.tokenStorageService.saveMe();
                }
            }
        });
    }

}