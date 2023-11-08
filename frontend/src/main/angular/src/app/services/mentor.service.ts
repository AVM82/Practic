import { Injectable } from "@angular/core";
import { Applicant, CourseApplicants } from "../models/applicant";
import { Observable } from "rxjs/internal/Observable";
import { HttpClient } from "@angular/common/http";
import { TokenStorageService } from "./token-storage.service";
import { ApiUrls, addMentorUrl, removeMentorUrl } from "../enums/api-urls";
import { StateStudent } from "../models/student";
import { Mentor, MentorComplex, StateMentor } from "../models/mentor";
import { User } from "../models/user";
import { Course } from "../models/course";

@Injectable({
    providedIn: 'root'
})
export class MentorService {
  
    constructor(
        private tokenStorageService: TokenStorageService,
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
            if (this.tokenStorageService.me!.maybeNewStudent(applicant))
                this.tokenStorageService.saveMe();
        });
    }

    reject(applicant: Applicant): void {
        console.log(applicant);
        this.http.post<Applicant>(ApiUrls.Applicants + `/reject/` + applicant.id, {}).subscribe(rejectedApplicant => {
            applicant.update(rejectedApplicant);
            console.log(applicant);
            if (this.tokenStorageService.me!.maybeNotApplicant(rejectedApplicant)) {
                console.log(' reject me');
                this.tokenStorageService.saveMe();
            }
        });
    }
    
    addMentor(course: Course, user: User): void {
        this.http.post<MentorComplex>(addMentorUrl(course.slug, user.id), {}).subscribe(complex => {
            course.mentors.push(complex.mentorDto);
            user.addMentor(complex.stateMentor);
            if (user.id == this.tokenStorageService.me!.id) {
                this.tokenStorageService.me!.addMentor(complex.stateMentor);
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
                    this.tokenStorageService.me!.removeMentorById(mentorId);
                    this.tokenStorageService.saveMe();
                }
            }
        });
    }

}