import {Component, OnInit} from '@angular/core';
import {STATE_FINISHED, STATE_NOT_STARTED, STATE_STARTED} from "../../enums/app-constans";
import {Quiz} from "../../models/quiz";
import {QuizService} from "../../services/quiz.service";
import {User} from "../../models/user";
import {TokenStorageService} from "../../services/token-storage.service";
import {ActivatedRoute} from "@angular/router";
import {CommonModule} from "@angular/common";
import {TimerService} from "../../services/timer.service";
import {TimerComponent} from "../timer/timer.component";
import {QuestionComponent} from "../question/question.component";
import {Question} from "../../models/question";
import {Answer} from "../../models/answer";

@Component({
    selector: 'app-quiz',
    standalone: true,
    templateUrl: './quiz.component.html',
    imports: [
        CommonModule,
        TimerComponent,
        QuestionComponent
    ],
    styleUrls: ['./quiz.component.css']
})
export class QuizComponent implements OnInit {
    quizState: string = '';
    quiz: Quiz | any;
    me!: User;
    quizId!: number;
    timerValue: number = 0;

    quizName: string = '';
    currentIndex: number = 0;
    questionsLength: number = 0;
    rightAnswers: number = 0;
    resultList: boolean[] = [];
    passed: boolean = false;
    currentSum: number = 0;
    barLine: string = '';

    protected readonly STATE_NOT_STARTED = STATE_NOT_STARTED;
    protected readonly STATE_STARTED = STATE_STARTED;
    protected readonly STATE_FINISHED = STATE_FINISHED;

    constructor(
        private quizService: QuizService,
        private tokenStorageService: TokenStorageService,
        private route: ActivatedRoute,
        private timerService: TimerService) {
        this.me = this.tokenStorageService.getMe();
    }

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            this.quizId = Number(params.get('quizId'));
            if (this.quizId > 0) {
                this.quizService.getQuiz(this.quizId)
                    .subscribe(quiz => {
                            this.quiz = quiz;
                            this.quizName = this.quiz.quizName;
                            this.questionsLength = this.quiz.questions.length;
                        }
                    );
                this.quizState = STATE_NOT_STARTED;
            }
        });
    }

    nextQuestion() {
        if (this.currentIndex < this.quiz.questions.length - 1) {
            this.currentIndex++;
        }
    }

    previousQuestion() {
        if (this.currentIndex > 0) {
            this.currentIndex--;
        }
    }

    startTimer(): void {
        console.log('quiz', this.quiz);
        console.log('quiz.questions.length', this.quiz.questions.length);
        this.timerValue = 60 * this.quiz.questions.length;
        this.timerService.startTimer(this.timerValue);
        this.setQuizState(STATE_STARTED);
    }

    setQuizState(state: string) {
        this.quizState = state;
    }

    selectedAnswer(questionIndex: number, answer: Answer) {
        this.quiz.questions[questionIndex].answers.forEach(
            (a: Answer) => a.correct = false
        );
        answer.correct = true;
    }

    goToQuestion(index: number) {
        this.currentIndex = index;
    }

    ifAnsweredQuestion(question: Question) {
        return question.answers.some(answer => answer.correct);
    }

    getResult() {
        // answer from UI
        const answersFromUI: number[] = [];
        this.quiz.questions.forEach((q: { answers: Answer[]; }) => {
            if (q.answers.some(answer => answer.correct)) {
                q.answers.forEach(a => {
                    if (a.correct) {
                        answersFromUI.push(a.id);
                    }
                })
            } else {
                answersFromUI.push(0);
            }
        })

        console.log('answersFromUI: ', answersFromUI);
        // answers from backend
        this.quizService.getResult(this.quizId, answersFromUI)
            .subscribe({
                    next: data => {
                        this.resultList = data;
                        // init right answers
                        this.resultList.forEach(b => {
                            if (b.valueOf()) {
                                this.rightAnswers = this.rightAnswers + 1;
                            }
                        });
                        // init text in finish page Ok or NOT  // it depends on 75% correct answers
                        if (((this.rightAnswers * 100) / this.quiz.questions.length) >= 75) {
                            this.passed = true;
                        }
                        this.setQuizState(STATE_FINISHED);
                        this.timerService.stopTimer();
                    }
                }
            );
    }

    redirectFromTest() {
        window.history.back();
    }

    handleTimerValueChange(timerValue: { minutes: number, seconds: number }): void {
        const total = this.quiz.questions.length * 60;
        const mark = 100 / total;
        this.barLine = `${this.currentSum}%`
        this.currentSum += mark;
    }

    handleTimerFinished() {
        this.getResult();
    }
}
