import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiUrls} from "../enums/api-urls";
import {httpOptions} from '../enums/app-constans';
import {Feedback} from "../models/feedback";

@Injectable({
    providedIn: 'root'
})
export class FeedbackService {
    constructor(private http: HttpClient) {}

    getFeedbacks(): Observable<Feedback[]> {
        return this.http.get<Feedback[]>(ApiUrls.Feedbacks);
    }

    postData(feedback: string): Observable<Feedback> {
        return this.http.post<Feedback>(ApiUrls.Feedbacks + `/`, feedback, httpOptions)
    }

    incrementLikes(feedback: Feedback): Observable<Feedback>{
      return this.http.patch<Feedback>(ApiUrls.Feedbacks  + `/add/` + feedback.id, {});
    }

    decrementLikes(feedback: Feedback): Observable<Feedback> {
        return this.http.patch<Feedback>(ApiUrls.Feedbacks + `/remove/` + feedback.id, {});
    }

    deleteFeedback(feedbackId: number): Observable<Feedback> {
        return this.http.delete<Feedback>(ApiUrls.Feedbacks + `/delete/` + feedbackId);
    }

}