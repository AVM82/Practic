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

    getAllApplicants(): Observable<CourseApplicants[]> {
        return this.http.get<CourseApplicants[]>(ApiUrls.Applicants);
    }

    getCourseApplicants(slug: string): Observable<CourseApplicants> {
        return this.http.get<CourseApplicants>(ApiUrls.Applicants + `/` + slug);
    }

    admitApplicant(id: number): Observable<StateStudent> {
        return this.http.post<StateStudent>(ApiUrls.Applicants + `/admit/` + id, {});
    }

    rejectApplicant(id: number): Observable<boolean> {
        return this.http.post<boolean>(ApiUrls.Applicants + `/reject/` + id, {});
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
        console.log('remove mentor id = ', mentorId);

        this.http.post<boolean>(removeMentorUrl(mentorId), {}).subscribe(success => {
            if (success) {
                course.mentors.slice(course.mentors.findIndex(mentor => mentor.id == mentorId) , 1);
                user.removeMentorById(mentorId);
                console.log('user:',user);
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