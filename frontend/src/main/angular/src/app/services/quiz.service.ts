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
        let queryParam = {'quizId': quizId};
        return this.http.get<Quiz>(
            ApiUrls.Quizzes + quizId, {params: queryParam});
    }

    getResult(quizId: number, ids: number[]): Observable<boolean[]> {
        return this.http.post<boolean[]>(ApiUrls.Quizzes + quizId, ids);
    }

    saveQuiz(quiz: Quiz) {
        window.sessionStorage.setItem('quiz', JSON.stringify(quiz));
    }

    getSavedQuiz(): Quiz | undefined {
        const quizJson = window.sessionStorage.getItem('quiz');
        return quizJson ? Quiz.fromJson(JSON.parse(quizJson)) : undefined;
    }
}
