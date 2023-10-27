import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { ApplyBtnComponent } from "src/app/componets/apply-btn/apply-btn.component";
import { CourseNavbarComponent } from "src/app/componets/course-navbar/course-navbar.component";
import { EditBtnComponent } from "src/app/componets/edit-btn/edit-course.component";
import { Course } from "src/app/models/course/course";
import { User } from "src/app/models/user/user";
import { CoursesService } from "src/app/services/courses/courses.service";

const safeReferenceStarts = '<a target="_blank" rel="noopener" href="';

@Component({
    selector: 'app-main-page',
    standalone: true,
    imports: [ CourseNavbarComponent, RouterLink, ApplyBtnComponent, EditBtnComponent],
    templateUrl: './main-page.component.html',
    styleUrls: ['./main-page.component.css']
  })
export class MainPageComponent implements OnInit {
    name: string = '';
    authorPresent: boolean = false;
    authors!: HTMLElement ;
    pagePresent: boolean = false;
    page!: HTMLElement ;
    mentorPresent: boolean = false;
    mentor!: HTMLElement ;

    constructor(
        private coursesService: CoursesService,
        private route: ActivatedRoute  
    ) {}
    
    ngOnInit(): void {
        this.authors = document.getElementById('author')!;
        this.mentor = document.getElementById('mentor')!;
        this.page = document.getElementById('page')!;
    }

    makeAuthor(author: string): string {
        let a:string[] = author.split('<>');
        return a.length > 1 ? safeReferenceStarts + a[1] + '">' + a[0] + '</a>' : a[0];
    }

    makeAuthorsList(authors: string[]): string {
        let list: string[] = [];
        authors.forEach(author => list.push(this.makeAuthor(author)));
        return list.join(', ');
    }

    makeMentor(mentor: User): string {
        return safeReferenceStarts + mentor.profilePictureUrl + '">' + mentor.name + '</a>';
    }

    makeMentorsList(mentors: User[]): string {
        let list: string[] = [];
        mentors.forEach(mentor => list.push(this.makeMentor(mentor)));
        return list.join(', ');
    }

    getPage(course: Course) {
        this.name = course.name;
        this.authorPresent = course.authors != null && course.authors.length > 0;
        if (this.authorPresent) 
            this.authors.innerHTML = 'Автор' + this.plurals(course.authors!) + ' : ' + this.makeAuthorsList(course.authors!);
        this.mentorPresent = course.mentors != null && course.mentors.length > 0;
        if (this.mentorPresent)
            this.mentor.innerHTML = 'Ментор' + this.plurals(course.mentors!) + ' : ' +this.makeMentorsList(course.mentors!);
        this.pagePresent = course.description != null;
        if (this.pagePresent)
            this.page.innerHTML = course.description!;
    }

    plurals(arr: any[]): string {
        return arr.length > 1 ? 'и' : '';
    }
}