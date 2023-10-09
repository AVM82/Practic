import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { ApplyBtnComponent } from "src/app/componets/apply-btn/apply-btn.component";
import { CourseNavbarComponent } from "src/app/componets/course-navbar/course-navbar.component";
import { EditBtnComponent } from "src/app/componets/edit-btn/edit-course.component";
import { ReferenceTitle } from "src/app/models/reference/referenceTitle";
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

    constructor(
        private coursesService: CoursesService,
        private route: ActivatedRoute  
    ) {}
    
    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            const slug = params.get('slug');
            if(slug) {
                this.coursesService.getCourse(slug).subscribe(course => {
                    this.name = course.name;
                    const author = document.getElementById('author');
                    if (course.authors && author) {
                        let many = course.authors.length > 1;
                        author.innerHTML = 'Автор' + (many ? 'и' : '') + ' : ' + (many ? this.makeAuthorsList(course.authors!) : this.makeA(course.authors![0]));
                    }
                });
                this.coursesService.getDescription(slug).subscribe(description => {
                    const part = document.getElementById('page');
                    if (part) 
                        part.innerHTML = description;
                });
            }
        });
    }

    makeA(author: string): string {
        let a:string[] = author.split('<>');
        return a.length > 1 ? '<a target="_blank" rel="noopener" href="' + a[1] + '">' + a[0] + '</a>' : a[0];
    }

    makeAuthorsList(authors: string[]): string {
        var list: string[] = [];
        authors.forEach(author => list.push(this.makeA(author)));
        return list.join(', ');
    }
}