import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ApiUrls} from "../../enums/api-urls";


@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  constructor(private http: HttpClient) { }

  getFeedbacks(): Observable<any> {
    return this.http.get(ApiUrls.Feedbacks);
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

  incrementLikes(id: number,email:string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = {
      email: email
    };
    const apiUrl = `${ApiUrls.Feedbacks}/${id}`;
  
    return this.http.patch(apiUrl, {body}, { headers: headers });
  }

  
  decrementLikes(id: number,email:string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = {
      id:id,
      email: email
    };
  
    return this.http.patch(ApiUrls.Feedbacks, {body}, { headers: headers });
  }
}