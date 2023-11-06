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
            applicant.isApplied = true;
            applicant.student = student;
            this.tokenStorageService.me!.removePossibleApplicant(applicant);
        });
    }

    reject(applicant: Applicant): Observable<boolean> {
        return this.http.post<boolean>(ApiUrls.Applicants + `/reject/` + applicant.id, {});
    }
    
    addMentor(course: Course, user: User): void {
        this.http.post<MentorComplex>(addMentorUrl(course.slug, user.id), {}).subscribe(complex => {
            course.mentors.push(complex.mentorDto);
            user.addMentor(complex.stateMentor);
            if (user.id == this.tokenStorageService.me!.id) {
                this.tokenStorageService.me!.addMentor(complex.stateMentor);
                this.tokenStorageService.saveUser(this.tokenStorageService.me!);
                console.log('me: added mentor')
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
                    this.tokenStorageService.saveUser(this.tokenStorageService.me!);
                    console.log('me: removed mentor')
                    console.log('me:', this.tokenStorageService.me!)
                }
            }
        });
    }

}