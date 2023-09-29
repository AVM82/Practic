import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ApiUrls} from "../../enums/api-urls";


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
    const headers = new HttpHeaders({'Content-Type':'application/json'});
    const body = {
      email: email,
      feedback: feedback
    };
        console.log(email);
    console.log(feedback);
    console.log("Feedback added");
  return this.http.post(ApiUrls.Feedbacks,body,{headers:headers})  
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
}