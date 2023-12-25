import {Question} from "./question";

export class Quiz {
    id: number = 0;
    quizName: string = '';
    questions: Question[] = [];
    state?: string;

    constructor(
        _id: number,
        _quizName: string,
        _questions: Question[],
        _state: string
    ) {
        this.id = _id;
        this.quizName = _quizName;
        this.questions = _questions;
        this.state = _state;
    }

    static fromJson(json: any): Quiz {
        return new Quiz(
            json.id,
            json.quizName,
            json.questions,
            json.state
        );
    }
}