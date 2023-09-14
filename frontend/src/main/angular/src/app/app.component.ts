import {Component} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './componets/header/header.component';
import {CourseNavbarComponent} from "./componets/course-navbar/course-navbar.component";
import { FooterComponent } from './componets/footer/footer.component';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    standalone: true,
    imports: [HeaderComponent, RouterOutlet, CourseNavbarComponent, FooterComponent]
})
export class AppComponent {
  title = 'Practic';
}