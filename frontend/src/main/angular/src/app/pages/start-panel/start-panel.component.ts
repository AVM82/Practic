import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { MatCardModule } from "@angular/material/card";
import { Router, RouterLink } from "@angular/router";
import { User } from "src/app/models/user/user";
import { TokenStorageService } from "src/app/services/auth/token-storage.service";

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
    isCollaborator: boolean = false;
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
            this.isAdmin = this.me.hasAnyRole('ADMIN');
            this.isCollaborator = this.me.hasAnyRole('COLLABORATOR');
            this.isMentor = this.me.hasAnyRole('MENTOR');
            this.isStudent = this.me.hasAnyRole('STUDENT');
            this.isGuest = this.me.hasAnyRole('GUEST');
        } else
            this.router.navigate(['/login']);
    }

    mentorPanel() {
        this.router.navigate(['/mentor']);
    }

}