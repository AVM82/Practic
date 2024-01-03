import {Component, OnInit} from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import {MatButtonModule} from "@angular/material/button";
import {TokenStorageService} from "../../services/token-storage.service";
import {NgIf} from "@angular/common";
import {environment} from "../../../enviroments/enviroment";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
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
  name: string = "User";
  profilePictureUrl = "";
  
  constructor(
      private tokenStorageService:TokenStorageService,
      private route: ActivatedRoute,
      private router:Router

      ) { }

  ngOnInit(): void {
    const user = this.tokenStorageService.getUser();
    if (user) {
      this.isAdmin = user.hasAdminRole();
      this.isAuthenticated = user.isAuthenticated;
      this.name = user.name;
      this.profilePictureUrl = user.profilePictureUrl;
    }
  }

  logout() {
    const logoutUrl = environment.logoutBaseUrl + 'logout';
    window.location.href = logoutUrl;
  }



  onMenuButtonClick(item: string) {
    if(item === 'person') {
      this.tokenStorageService.refreshMe();
    }
    if(item === 'logout') {
      this.logout();
    }
    if(item === 'profile') {
      this.router.navigate(['profile']);
    }
  }
}
