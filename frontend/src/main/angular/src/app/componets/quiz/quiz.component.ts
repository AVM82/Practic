import {Component, OnInit} from '@angular/core';
import {CORRECT_ANSWERS_PERCENT, STATE_FINISHED, STATE_NOT_STARTED, STATE_STARTED} from "../../enums/app-constans";
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
    quiz: Quiz;
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

    studentChapterId: number = 0;
    quizResultId: number = 0;
    time: number = 0;

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
            this.studentChapterId = Number(params.get('studentChapterId'));
            if (this.quizId > 0 && this.studentChapterId > 0) {
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

    startQuiz(): void {
        this.quizService.getQuizResultId(this.studentChapterId)
            .subscribe(id => this.quizResultId = id);
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
        this.quizService.getResult(this.quizId, this.quizResultId, answersFromUI, this.time)
            .subscribe({
                    next: data => {
                        this.resultList = data;
                        this.resultList.forEach(b => {
                            if (b.valueOf()) {
                                this.rightAnswers = this.rightAnswers + 1;
                            }
                        });
                        if (((this.rightAnswers * 100) / this.quiz.questions.length) >= CORRECT_ANSWERS_PERCENT) {
                            this.passed = true;
                        }
                        this.setQuizState(STATE_FINISHED);
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
        this.time = total - (timerValue.minutes * 60 + timerValue.seconds);
    }

    handleTimerFinished() {
        this.getResult();
    }
}
