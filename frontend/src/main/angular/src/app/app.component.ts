import {Component, OnDestroy} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './componets/header/header.component';
import {CourseNavbarComponent} from "./componets/course-navbar/course-navbar.component";
import { FooterComponent } from './componets/footer/footer.component';
import {MatSnackBar} from "@angular/material/snack-bar";
import {Subscription} from "rxjs";
import {InfoMessagesService} from "./services/info-messages.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    standalone: true,
    imports: [HeaderComponent, RouterOutlet, CourseNavbarComponent, FooterComponent]
})
export class AppComponent implements OnDestroy{
  title = 'Practic';
  message: any;
  subscription: Subscription;

  constructor(private _snackBar: MatSnackBar, private messagesService :InfoMessagesService) {
    this.subscription = messagesService.getMessage().subscribe(message => {
      this.message = message;
      if (this.message.text) {
        this.openSnackBar(this.message.text, this.message.type);
      }
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  openSnackBar(message: string, type: string) {
    this._snackBar.open(message, 'Закрити', {
      duration: 50000,
      panelClass: type === 'error' ? 'error-snackbar' : 'normal-snackbar'
    });
  }
 
}