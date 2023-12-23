import {Component, OnInit} from "@angular/core";
import {Profile} from "../../models/profile";
import {CommonModule, NgOptimizedImage} from "@angular/common";
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {ApplyBtnComponent} from "../../componets/apply-btn/apply-btn.component";
import {EditBtnComponent} from "../../componets/edit-btn/edit-course.component";
import {PersonService} from "../../services/person.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

@Component({
    selector:'app-my-profile',
    standalone:true,
    imports: [CommonModule, CourseNavbarComponent, MatCardModule, MatIconModule, ApplyBtnComponent,
        EditBtnComponent, FormsModule, NgOptimizedImage, ReactiveFormsModule ],
    templateUrl:'my-profile-page.html',
    styleUrls:['my-profile-page.css']
})

export class MyProfilePage implements OnInit{
    originalProfile!: Profile;
    profile!:Profile;
    constructor(private personService: PersonService) {}

    ngOnInit(): void {
        this.loadProfile();
    }

    loadProfile(): void {
         this.personService.loadProfile().subscribe(
        data=> {
            this.profile = { ...data };
            this.originalProfile =  { ...data };
        }
        );
    }
    saveChanges(): void {
        this.personService.saveProfile(this.profile).subscribe(
            data => {
                this.originalProfile = data;
                this.profile = data;
            },

        );
    }
    cancelChanges(): void {
        this.profile = { ...this.originalProfile };
    }

}

