import { Component } from '@angular/core';
import {  MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
  standalone: true,
  imports:[MatIconModule]
})

export class FooterComponent {
}
