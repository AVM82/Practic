import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { ApplyBtnComponent } from "src/app/componets/apply-btn/apply-btn.component";
import { CourseNavbarComponent } from "src/app/componets/course-navbar/course-navbar.component";
import { EditBtnComponent } from "src/app/componets/edit-btn/edit-course.component";
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
                    if (course.authors)
                        document.getElementById('author')!.innerHTML = 'Автор' + (course.authors.length > 1 ? 'и' : '') + ' : ' + this.makeAuthorsList(course.authors!);
                });
                this.coursesService.getDescription(slug).subscribe(description =>
                    document.getElementById('page')!.innerHTML = description
                );
            }
        });
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
}