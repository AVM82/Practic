import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import {RouterLink} from "@angular/router";
import {MatButtonModule} from "@angular/material/button";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css'],
    standalone: true,
  imports: [MatToolbarModule, MatIconModule, RouterLink, MatButtonModule]
})

export class HeaderComponent {

}
