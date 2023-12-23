import {Answer} from "./answer";

export class Question {
    id: number = 0;
    question: string = '';
    answers: Answer[] = [];

    constructor(
        _id: number,
        _question: string,
        _answers: Answer[]
    ) {
        this.id = _id;
        this.question = _question;
        this.answers = _answers;
    }

    static fromJson(json: any): Question {
        return new Question(
            json.id,
            json.question,
            json.answers
        );
    }
}