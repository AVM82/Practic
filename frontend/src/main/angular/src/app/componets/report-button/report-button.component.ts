import {Component, Input} from "@angular/core";
import {Router, RouterLink} from "@angular/router";
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
  @Input() reportsNumber!: number


  isReportsPresent(): boolean {
    if(this.reportsNumber>0) {
      this.dropdownText = "Заплановано " + this.reportsNumber + " доповіді/ей"
      return false;
    }
    return true;
  }
  navigate(event: Event) {
    event.stopPropagation();
    this.router.navigate(['/reports']);
  }

}
