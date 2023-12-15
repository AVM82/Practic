import {Component} from '@angular/core';
import {Answer} from "../../models/answer";

@Component({
  selector: 'app-answer',
  standalone: true,
  templateUrl: './answer.component.html',
  styleUrls: ['./answer.component.css']
})
export class AnswerComponent {
  answer!: Answer;
}
