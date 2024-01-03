import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Quiz} from "../models/quiz";
import {ApiUrls} from "../enums/api-urls";

@Injectable({
    providedIn: 'root'
})
export class QuizService {

    constructor(private http: HttpClient) {
    }

    getQuiz(quizId: number): Observable<Quiz> {
        return this.http.get<Quiz>( ApiUrls.Quizzes + quizId);
    }

    getResult(quizId: number, quizResultId: number, ids: number[][], time: number): Observable<boolean[]> {
        return this.http.post<boolean[]>(ApiUrls.Quizzes + quizId + '/' + quizResultId + '/' + time, ids);
    }

    saveQuiz(quiz: Quiz) {
        window.sessionStorage.setItem('quiz', JSON.stringify(quiz));
    }

    getQuizResultId(studentChapterId: number): Observable<number> {
        return this.http.get<number>(ApiUrls.Quizzes + 'start/' + studentChapterId);
    }

    getOriginQuiz(quizId: number): Observable<Quiz> {
        return  this.http.get<Quiz>(ApiUrls.Quizzes + quizId + '/origin')
    }

    getSavedResult(quizId: number, quizResultId: number): Observable<number[][]> {
        return this.http.get<number[][]>(ApiUrls.Quizzes + quizId + '/result/' + quizResultId);
    }
}
