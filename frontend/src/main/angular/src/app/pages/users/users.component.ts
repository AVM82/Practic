import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { MatMenuModule } from "@angular/material/menu";
import { Router } from "@angular/router";
import { AngularSvgIconModule } from "angular-svg-icon";
import { MANUALLY_CHANGED_ROLES, Roles } from "src/app/enums/app-constans";
import { Course } from "src/app/models/course";
import { User } from "src/app/models/user";
import { CoursesService } from "src/app/services/courses.service";
import { MentorService } from "src/app/services/mentor.service";
import { PersonService } from "src/app/services/person.service";
import { TokenStorageService } from "src/app/services/token-storage.service";

@Component({
    selector: 'app-users',
    standalone: true,
    imports: [CommonModule, MatMenuModule, AngularSvgIconModule],
    templateUrl: './users.component.html',
    styleUrls: ['./users.component.css']
})
 export class UsersComponent implements OnInit {
    me!: User;
    users: User[] = [];
    courses: Course[] = [];
    personService: PersonService;
    mentorService: MentorService;
    surnameFirst: boolean = false;
    isStaff: boolean = false;
    roles = MANUALLY_CHANGED_ROLES;
    ADMIN = Roles.ADMIN;
    STAFF = Roles.STAFF;
    
    constructor(
        private tokenStorage: TokenStorageService,
        private courseService: CoursesService,
        private personService0: PersonService,
        private mentorService0: MentorService,
        private router: Router
    ) {
        this.me = tokenStorage.getMe();
        if (!this.me.hasAnyRole(Roles.ADMIN, Roles.STAFF))
          router.navigate(['']);
        this.isStaff = this.me.hasStaffRole();
        this.personService = personService0;
        this.mentorService = mentorService0;
    }
    
    ngOnInit(): void {
        this.personService.getAllUsers(this.users);
        this.courseService.getAllCourses().subscribe(courses => {
            this.courses = courses
        });
    }
  
    private swapNameSurname() : void {
        this.users?.forEach(user => user.swapNameSurname());
        this.surnameFirst = !this.surnameFirst;
    }

    private sortByNameField(descending: boolean): void {
        this.users?.sort((a, b) => a.name.localeCompare(b.name) * (descending ? -1 : 1));
    }

    sortByName(descending: boolean): void {
        if (this.surnameFirst) 
            this.swapNameSurname();
        this.sortByNameField(descending);
    }

    sortBySurname(descending: boolean): void {
        if (!this.surnameFirst) 
            this.swapNameSurname();
        this.sortByNameField(descending);
    }
  
}

  