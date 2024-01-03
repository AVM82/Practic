import {Question} from "./question";

export class Quiz {
    id: number = 0;
    quizName: string = '';
    questions: Question[] = [];
    state?: string;
}
