import {Component, Input} from "@angular/core";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
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
  rep = " доповідь";
  constructor(private router: Router,
  private route: ActivatedRoute) {}
  @Input() reportsNumber!: number


  isReportsPresent(): boolean {
    if(this.reportsNumber>0) {
      if(this.reportsNumber>1&&this.reportsNumber<5){
        this.rep = ' доповіді'
      }
      if(this.reportsNumber>=5){
        this.rep = ' доповідей'
      }
      this.dropdownText = "Заплановано " + this.reportsNumber + this.rep
      return false;
    }
    return true;
  }
  navigate(event: Event) {
    event.stopPropagation();
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug');
      if (slug) {
        this.router.navigate(['courses/',slug,'reports']);
      }
    })
  }

}
