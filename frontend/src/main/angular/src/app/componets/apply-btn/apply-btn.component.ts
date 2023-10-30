import {Component, Input, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {InfoMessagesService} from "../../services/info-messages.service";
import { User } from 'src/app/models/user/user';
import { PersonService } from 'src/app/services/person/person.service';
import { StateApplicant } from 'src/app/models/user/applicant';

const allowedUpdatePeriodMs = 1000;

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
  stateApplicant?: StateApplicant;
  email!: string;
  me!: User;
  timeStampMs: number = 0;
  neitherStudentNorMentor: boolean = true;

  constructor(
      private personService: PersonService,
      private messagesService: InfoMessagesService
  ) { }

  ngOnInit() {
    this.me = this.personService.me;
    //поки що треба перезайти щоб оновити
    this.stateApplicant = this.me.getApplicant(this.slug);
    if (this.stateApplicant) {
      this.timeStampMs = Date.now();
      this.isApplicant = true;
    }
  }

  setCutoffTimeStampMs(): boolean {
    const time = Date.now();
    if (time > this.timeStampMs) {
      this.timeStampMs = time + allowedUpdatePeriodMs;
      return true;
    }
    return false;
  }

  onApplyClick() {
    if (confirm("Ви дійсно хочете записатися на цей курс?"))
      this.personService.createApplication(this.slug).subscribe({
        next: applicant => {
          console.log('applicant (',this.slug, ') = ', applicant);
          this.me = this.personService.addStateAplicant(applicant);
          this.stateApplicant = this.me.getApplicant(this.slug);
          this.messagesService.showMessage("Заявка прийнята", "normal");
          this.isApplicant = true;
          this.setCutoffTimeStampMs();
        },
        error: error => {
          console.error('Помилка при відправці заявки', error);
          this.messagesService.showMessage("Помилка відправки заявки", "error")
        }
      });
  }

  checkApplied() {
    if (this.setCutoffTimeStampMs())
      this.personService.checkApplicantState(this.stateApplicant!.applicantId).subscribe(isApplied => {
        if (isApplied) {
          this.neitherStudentNorMentor = false;
          this.personService.newStudent(this.slug).subscribe(stateStudent => {
            this.personService.addStateStudent(stateStudent!);
            window.location.href = window.location.origin
                   + `/courses/` + this.slug + `/chapters` + stateStudent!.activeChapterNumber;
          })
        }
      })
  }
}
