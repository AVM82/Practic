import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ApiUrls} from "../enums/api-urls";
import { httpOptions } from '../enums/app-constans';


@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  constructor(private http: HttpClient) { }

  getFeedbacks(feedbackSortedState:string): Observable<any> {
    const apiUrl = `${ApiUrls.Feedbacks}${"?feedbackSort="+feedbackSortedState}`;

    return this.http.get(apiUrl);
  }

  postData(email:string, feedback:string):  Observable<any>{
    const body = {
      email: email,
      feedback: feedback
    };
        console.log(email);
    console.log(feedback);
    console.log("Feedback added");
  return this.http.post(ApiUrls.Feedbacks,body, httpOptions)  
  }

  incrementLikes(feedbackId: number,personId:number): Observable<any> {
    const apiUrl = `${ApiUrls.Feedbacks}${feedbackId}`;
  
    return this.http.patch(apiUrl, personId);
  }

  
  decrementLikes(feedbackId: number,personId:number): Observable<any> {
    const body = {
      feedbackId:feedbackId,
      personId: personId
    };
  
    return this.http.patch(ApiUrls.Feedbacks, body);
  }

  deleteFeedback(feedbackId: number):Observable<any>{
    const apiUrl = `${ApiUrls.Feedbacks}${"?idFeedback="+feedbackId}`;
  
    return this.http.delete(apiUrl);
  }

}