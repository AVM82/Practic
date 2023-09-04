import {Component, OnInit} from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import {MatButtonModule} from "@angular/material/button";
import {TokenStorageService} from "../../services/auth/token-storage.service";
import {User} from "../../services/auth/auth.service";
import {NgIf} from "@angular/common";
import {environment} from "../../../enviroments/enviroment";
import {Router, RouterLink} from "@angular/router";
import {MenuBtnComponent} from "../menu-btn/menu-btn.component";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css'],
    standalone: true,
  imports: [MatToolbarModule, MatIconModule, MatButtonModule, NgIf, RouterLink, MenuBtnComponent]
})

export class HeaderComponent implements OnInit{

  isAdmin: boolean = false;
  isAuthenticated: boolean = false;

  constructor(
      private tokenStorageService:TokenStorageService,
      private router: Router
  ) {
  }
  ngOnInit(): void {
    const token = this.tokenStorageService.getToken();
    if (token) {
      const user: User = this.tokenStorageService.getUser();
      let currentUser: User = new User(user.roles);
      this.isAdmin = currentUser.hasAnyRole('ADMIN');
      this.isAuthenticated = currentUser.isAuthenticated;
    }
  }

  logout() {
    const logoutUrl = environment.logoutBaseUrl + 'logout';
    window.location.href = logoutUrl;
  }

  toAdminPage() {
    this.router.navigate(['/admin']);
  }

  onMenuButtonClick(item: string) {
    if(item === 'logout') {
      this.logout();
    }
    if(item === 'admin') {
      this.toAdminPage();
    }
  }
}
