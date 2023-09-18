import { Component } from '@angular/core';
import {  MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
  standalone: true,
  imports:[MatIconModule]
})

export class FooterComponent {
  constructor(private router: Router) { }

  goToFeedbackPage() {
    this.router.navigate(['/feedback']);
  }

}
