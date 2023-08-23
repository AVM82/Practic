import {Component} from "@angular/core";
import {Router, RouterLink} from "@angular/router";
/*import {MatBadge, MatBadgeModule} from "@angular/material/badge";*/
import {CommonModule, NgFor, NgIf} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";


@Component({
  selector: 'report-button',
  standalone: true,
  templateUrl: './report-button.component.html',
  imports: [CommonModule, NgFor, NgIf, MatIconModule, MatButtonModule, RouterLink,  MatButtonModule, MatButtonModule],
  styleUrls: ['/report-button.component.css']
})
export class ReportButtonComponent{

  dropdownText = "Доповідей не заплановано";
  constructor(private router: Router) {}

  navigate(event: Event) {
    event.stopPropagation();
    this.router.navigate(['/reports']);
  }
  isEmpty(): boolean {
    return false;
  }
}
