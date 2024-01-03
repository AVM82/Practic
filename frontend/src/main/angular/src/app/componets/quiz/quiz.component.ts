import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {
    CORRECT_ANSWERS_PERCENT,
    STATE_FINISHED,
    STATE_NOT_STARTED,
    STATE_QUIZ_RESULT,
    STATE_STARTED
} from "../../enums/app-constans";
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
import {Chapter} from "../../models/chapter";

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
    @Input() studentChapter!: Chapter;
    @Input() isQuizResultVisible!: boolean;
    @Output() closeQuiz: EventEmitter<any> = new EventEmitter();

    quizState: string = '';
    quiz!: Quiz;
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
    answersFromUI: number[][] = [];
    quizResult: Quiz = new Quiz();
    originQuiz!: Quiz;
    answersResult!: number[][];

    studentChapterId: number = 0;
    quizResultId: number = 0;
    time: number = 0;

    protected readonly STATE_NOT_STARTED = STATE_NOT_STARTED;
    protected readonly STATE_STARTED = STATE_STARTED;
    protected readonly STATE_FINISHED = STATE_FINISHED;
    protected readonly STATE_QUIZ_RESULT = STATE_QUIZ_RESULT;
    protected readonly CORRECT_ANSWERS_PERCENT = CORRECT_ANSWERS_PERCENT;

    constructor(
        private quizService: QuizService,
        private tokenStorageService: TokenStorageService,
        private route: ActivatedRoute,
        private timerService: TimerService) {
        this.me = this.tokenStorageService.getMe();
    }

    ngOnInit(): void {
        this.quizId = this.studentChapter.quizId;
        this.studentChapterId = this.studentChapter.id;
        if (this.quizId > 0 && this.studentChapterId > 0) {

            // get quiz from backend (all answers = false)
            this.quizService.getQuiz(this.quizId)
                .subscribe(quiz => {
                    this.quiz = quiz;
                    this.quizName = this.quiz.quizName;
                    this.questionsLength = this.quiz.questions.length;
                    // Condition -> if test was passed
                    if (!this.isQuizResultVisible) {
                        // Go to quiz
                        this.setQuizState(STATE_NOT_STARTED);
                    } else {
                        this.quizResultId = this.studentChapter.quizResultId;
                        // Load student answers from backend
                        this.quizService.getSavedResult(this.quizId, this.quizResultId)
                            .subscribe(answers => {
                                this.answersResult = answers;
                                // Init this.quiz via student answers
                                this.quiz.questions.forEach((q, i) => {
                                    q.answers.forEach((a,j) => {
                                        this.answersResult.forEach(list => {
                                            if (list.includes(a.id)) {
                                                this.quiz.questions[i].answers[j].correct = true;
                                            }
                                        })
                                    })
                                })
                                this.initQuizResult();
                                // Go to quiz result
                                this.setQuizState(STATE_QUIZ_RESULT);
                            });

                    }
                });
        }
    }

    private initQuizResult() {
        this.currentIndex = 0;
        Object.assign(this.quizResult, this.quiz);
        this.quizService.getOriginQuiz(this.quizId).subscribe(originQuiz => {
            this.originQuiz = originQuiz;
            this.originQuiz.questions.forEach((originQuestion, i) => {
                originQuestion.answers.forEach((originAnswer, j) => {
                    const quizResultAnswer = this.quizResult.questions[i].answers[j];
                    if (!originAnswer.correct && !quizResultAnswer.correct) {
                        quizResultAnswer.color = 'white';
                    } else if (!originAnswer.correct && quizResultAnswer.correct) {
                        quizResultAnswer.color = 'red';
                    } else {
                        quizResultAnswer.color = 'green';
                    }
                });
            });
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
            .subscribe(id => {
                this.quizResultId = id;
                this.studentChapter.quizResultId = id;
            });
        this.timerValue = 60 * this.quiz.questions.length;
        this.timerService.startTimer(this.timerValue);
        this.setQuizState(STATE_STARTED);
    }

    setQuizState(state: string) {
        this.quizState = state;
    }

    selectedAnswer(questionIndex: number, answer: Answer) {
        answer.correct = !answer.correct;
    }

    goToQuestion(index: number) {
        this.currentIndex = index;
    }

    ifAnsweredQuestion(question: Question) {
        return question.answers.some(answer => answer.correct);
    }

    getResult() {
        this.quiz.questions.forEach((q: { answers: Answer[]; }) => {
            if (q.answers.some(answer => answer.correct)) {
                let list: number[] = [];
                q.answers.forEach(a => {
                    if (a.correct) {
                        list.push(a.id)
                    }
                })
                this.answersFromUI.push(list);
                list = [];
            } else {
                let list: number[] = [0];
                this.answersFromUI.push(list);
            }
        })

        this.quizService.getResult(this.quizId, this.quizResultId, this.answersFromUI, this.time)
            .subscribe({
                    next: data => {
                        this.resultList = data;
                        this.resultList.forEach(b => {
                                if (b.valueOf()) {
                                    this.rightAnswers = this.rightAnswers + 1;
                                }
                            }
                        );
                        if (((this.rightAnswers * 100) / this.quiz.questions.length) >= CORRECT_ANSWERS_PERCENT) {
                            this.passed = true;
                            this.studentChapter.quizPassed = true;
                        }
                        this.setQuizState(STATE_FINISHED);
                    }
                }
            );
    }

    redirectFromTest(passed: boolean) {
        if (passed) {
            this.studentChapter.isQuizPassed = true;
        }
        this.closeQuiz.emit();
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
