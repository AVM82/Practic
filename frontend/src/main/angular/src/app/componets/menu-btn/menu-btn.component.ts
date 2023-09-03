import {Component, EventEmitter, Input, Output} from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatIconModule} from "@angular/material/icon";
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'app-menu-btn',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatMenuModule, MatButtonModule],
  templateUrl: './menu-btn.component.html',
  styleUrls: ['./menu-btn.component.css']
})
export class MenuBtnComponent {
  @Output() menuItemSelected = new EventEmitter<string>();
  @Input() isAdmin :boolean = false;

  onMenuItemClick(item: string) {
    this.menuItemSelected.emit(item);
  }
}
