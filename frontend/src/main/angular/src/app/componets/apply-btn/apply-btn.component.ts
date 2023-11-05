import {Component, Input, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { User } from 'src/app/models/user';
import { PersonService } from 'src/app/services/person.service';
import { StateApplicant } from 'src/app/models/applicant';

const allowedUpdatePeriodMs = 5000;

@Component({
  selector: 'app-apply-btn',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './apply-btn.component.html',
  styleUrls: ['./apply-btn.component.css']
})
export class ApplyBtnComponent implements OnInit {
  @Input() slug: string = '';
  isApplicant: boolean = false;
  me!: User;
  timeStampMs: number = 0;

  constructor(
      private personService: PersonService
  ) { }

  ngOnInit() {
    this.me = this.personService.me;
    this.isApplicant = this.me.isApplicant(this.slug);
  }

  checkIsNotStudent(): boolean {
    if (this.me.isStudent(this.slug))
      window.location.href = window.location.origin + `/courses/` + this.slug + `/chapters` + this.me.getCourseActiveChapterNumber(this.slug);
    return true;
  }

  onApplyClick() {
      this.personService.createApplication(this.slug);
  }

  checkApplied() {
    this.isApplicant = this.me.isApplicant(this.slug);
    if (this.isApplicant) {
      const time = Date.now();
      if (time > this.timeStampMs) {
        this.timeStampMs = time + allowedUpdatePeriodMs;
          this.personService.checkApplicant(this.me.getApplicant(this.slug)!.applicantId);
      }
    }
  }
}
