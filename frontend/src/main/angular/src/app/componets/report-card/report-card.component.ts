import {Component, Input} from "@angular/core";
import {CommonModule, NgFor, NgIf} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'report-card',
  standalone: true,
  templateUrl: './report-card.component.html',
  imports: [CommonModule, NgFor, NgIf, MatIconModule, MatButtonModule, RouterLink, MatButtonModule, MatButtonModule, MatCardModule],
  styleUrls: ['/report-card.component.css']
})
export class ReportCardComponent {
  //todo split dateTimeValue to separate date and time inputs
  @Input() reportTopic!: string
  @Input() studentName!: string
  @Input() dateTimeValue!: string
  @Input() profilePictureUrl!: string

  get datePart(): string {
    return this.dateTimeValue.split('T')[0];
  }

  get timePart(): string {
    return this.dateTimeValue.split('T')[1];
  }

}