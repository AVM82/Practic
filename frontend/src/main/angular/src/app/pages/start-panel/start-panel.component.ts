import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { MatCardModule } from "@angular/material/card";
import { Router, RouterLink } from "@angular/router";
import { User } from "src/app/models/user";
import { TokenStorageService } from "src/app/services/token-storage.service";

@Component({
    selector: 'app-course-details',
    standalone: true,
    imports: [CommonModule, MatCardModule, RouterLink],
    templateUrl: './start-panel.component.html',
    styleUrls: ['./start-panel.component.css']
  })
export class StartPanelComponent {
    router!: Router;
    me!: User;
    isAdmin: boolean = false;
    isStaff: boolean = false;
    isMentor: boolean = false;
    isStudent: boolean = false;
    isGuest: boolean = false;

    constructor(
        private router0: Router,
        private tokenStorageService: TokenStorageService
    ) {
        this.router = router0;
        this.me = tokenStorageService.getMe();
        if (this.me) {
            this.isAdmin = this.me.hasAdminRole();
            this.isStaff = this.me.hasStaffRole();
            this.isMentor = this.me.hasMentorRole();
            this.isStudent = this.me.hasStudentRole();
            this.isGuest = this.me.hasGuestRole();
        }
    }

}