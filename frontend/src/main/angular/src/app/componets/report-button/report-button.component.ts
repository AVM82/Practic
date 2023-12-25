import {Component, Input} from "@angular/core";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {CommonModule, NgFor, NgIf} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import { Report } from 'src/app/models/report';
import { ReportState } from "src/app/enums/app-constans";

@Component({
  selector: 'report-button',
  standalone: true,
  templateUrl: './report-button.component.html',
  imports: [CommonModule, NgFor, NgIf, MatIconModule, MatButtonModule, RouterLink,  MatButtonModule, MatButtonModule],
  styleUrls: ['/report-button.component.css']
})
export class ReportButtonComponent{
  @Input() reports!: Report[];
  @Input() slug!: string;
  dropdownText: string[] = ["Доповідей не заплановано"];
  rep = " доповідь";

  constructor(private router: Router,
  private route: ActivatedRoute) {}
  

  isReportsPresent(): boolean {
    if(this.reports.length > 0) {
      this.dropdownText = [];
      this.dropdownText.push("план: " + this.reports.filter(report => report.state === ReportState.ANNOUNCED).length )
      this.dropdownText.push( " зараз: " + this.reports.filter(report => report.state === ReportState.STARTED).length)
      this.dropdownText.push( " було: " + this.reports.filter(report => report.state === ReportState.FINISHED || report.state === ReportState.APPROVED).length);
      return false;
    }
    return true;
  }
  
  navigate(event: Event) {
    event.stopPropagation();
    this.router.navigate(['courses', this.slug, 'reports']);
  }

}
