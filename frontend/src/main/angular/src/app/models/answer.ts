
export class Answer {
    id: number = 0;
    answer: string = '';
    correct: boolean;
    color: string = '';

    constructor(
        _id: number,
        _answer: string,
        _correct: boolean,
        _color: string
    ) {
        this.id = _id;
        this.answer = _answer;
        this.correct = _correct;
        this.color = _color;
    }

    static fromJson(json: any): Answer {
        return new Answer(
            json.id,
            json.answer,
            json.correct,
            json.color
        );
    }
}
