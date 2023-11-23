import {Component, Input, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { User } from 'src/app/models/user';
import { PersonService } from 'src/app/services/person.service';
import { StateApplicant } from 'src/app/models/applicant';
import { TokenStorageService } from 'src/app/services/token-storage.service';

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
  applicant?: StateApplicant;
  me!: User;
  timeStampMs: number = 0;

  constructor(
    private tokenStorageService: TokenStorageService,
    private personService: PersonService
  ) { }

  ngOnInit() {
    this.me = this.tokenStorageService.me!;
    this.applicant = this.me.getApplicant(this.slug);
  }

  checkIsNotStudent(): boolean {
    let student = this.me.getStudent(this.slug);
    if (student)
      window.location.href = window.location.origin + `/courses/` + this.slug + `/chapters/` + student.activeChapterNumber;
    return true;
  }

  onApplyClick() {
      this.personService.createApplication(this.slug);
  }

  checkApplied() {
    this.applicant = this.me.getApplicant(this.slug);
    if (this.applicant) {
      const time = Date.now();
      if (time > this.timeStampMs) {
        this.timeStampMs = time + allowedUpdatePeriodMs;
          this.personService.checkApplicant(this.applicant.applicantId);
      }
    }
  }
}
