import {Component} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './componets/header/header.component';
import {CourseNavbarComponent} from "./componets/course-navbar/course-navbar.component";


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    standalone: true,
  imports: [HeaderComponent, RouterOutlet, CourseNavbarComponent]
})
export class AppComponent {
  title = 'Practic';
}