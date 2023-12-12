import { Injectable } from '@angular/core';
import {NewStudentReport} from "../../models/newStudentReport/newStudentReport";
import {catchError, Observable, of} from "rxjs";
import {ApiUrls} from "../../enums/api-urls";
import {StudentReport} from "../../models/report/studentReport";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class CalendarEventService {


  constructor(private http: HttpClient,
              private router: Router) { }

  headers = new HttpHeaders({
    'Content-Type': 'application/json',
  });

  sendEmailNotification(newEvent: any):Observable<any> {
    return this.http.post<NewStudentReport>(
        ApiUrls.CalendarEventEmailNotification, JSON.stringify(newEvent), { headers: this.headers } )
        .pipe(catchError(this.handleError<StudentReport>(`send notification of creating report = `)));

  }
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error);
      console.error(`${operation} failed: ${error.message}`);

      this.router.navigateByUrl('/404');

      return of(result as T);
    };
  }
}
