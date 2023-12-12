import {Component} from '@angular/core';
import {Question} from "../../models/question";
import {AnswerComponent} from "../answer/answer.component";

@Component({
  selector: 'app-question',
  standalone: true,
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.css'],
  imports: [
      AnswerComponent
  ]
})
export class QuestionComponent {
  question!: Question;
}
