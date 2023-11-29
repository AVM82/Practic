import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiUrls} from "../enums/api-urls";
import {httpOptions} from '../enums/app-constans';
import {Feedback, FeedbackPage} from "../models/feedback";

@Injectable({
    providedIn: 'root'
})
export class FeedbackService {
    constructor(private http: HttpClient) {}

    getFeedbacks(page: number, size: number, sortState: string): Observable<FeedbackPage> {
        const params = new HttpParams()
            .set('page', page.toString())
            .set('size', size.toString())
            .set('sortState', sortState);

        return this.http.get<FeedbackPage>(ApiUrls.Feedbacks, { params });
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

    deleteFeedback(feedbackId: number, page: number, size: number, sortState: string): Observable<FeedbackPage> {
        const params = new HttpParams()
            .set('page', page.toString())
            .set('size', size.toString())
            .set('sortState', sortState);
        return this.http.delete<FeedbackPage>(ApiUrls.Feedbacks + `/delete/` + feedbackId, {params});
    }

}