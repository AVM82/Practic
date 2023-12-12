
export class Answer {
    id: number = 0;
    answer: string = '';
    correct: boolean = false;

    constructor(
        _id: number,
        _answer: string,
        _correct: boolean
    ) {
        this.id = _id;
        this.answer = _answer;
        this.correct = _correct;
    }

    static fromJson(json: any): Answer {
        return new Answer(
            json.id,
            json.answer,
            json.correct
        );
    }
}