import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { ApplyBtnComponent } from "src/app/componets/apply-btn/apply-btn.component";
import { CourseNavbarComponent } from "src/app/componets/course-navbar/course-navbar.component";
import { EditBtnComponent } from "src/app/componets/edit-btn/edit-course.component";
import { Course } from "src/app/models/course/course";
import { CoursesService } from "src/app/services/courses/courses.service";

@Component({
    selector: 'app-main-page',
    standalone: true,
    imports: [ CourseNavbarComponent, RouterLink, ApplyBtnComponent, EditBtnComponent],
    templateUrl: './main-page.component.html',
    styleUrls: ['./main-page.component.css']
  })
export class MainPageComponent implements OnInit {
    name: string = '';
    authors!: HTMLElement ;
    page!: HTMLElement ;

    constructor(
        private coursesService: CoursesService,
        private route: ActivatedRoute  
    ) {}
    
    ngOnInit(): void {
        this.authors = document.getElementById('author')!;
        this.page = document.getElementById('page')!;
    }

    makeA(author: string): string {
        let a:string[] = author.split('<>');
        return a.length > 1 ? '<a target="_blank" rel="noopener" href="' + a[1] + '">' + a[0] + '</a>' : a[0];
    }

    makeAuthorsList(authors: string[]): string {
        let list: string[] = [];
        authors.forEach(author => list.push(this.makeA(author)));
        return list.join(', ');
    }

    getPage(course: Course) {
        this.name = course.name;
        this.authors.innerHTML = 'Автор' + (course.authors!.length > 1 ? 'и' : '') + ' : ' + this.makeAuthorsList(course.authors!);
        this.page.innerHTML = course.description!;
    }
}