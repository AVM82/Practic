import { Injectable } from '@angular/core';
import {NewStudentReport} from "../models/newStudentReport";
import {catchError, Observable, of} from "rxjs";
import { sendCalendarEventEmailNotificationUrl} from "../enums/api-urls";
import {StudentReport} from "../models/report";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class CalendarEventService {


  constructor(private http: HttpClient,
              private router: Router) { }

  sendEmailNotification(slug: string, newEvent: any):Observable<any> {
    return this.http.post<NewStudentReport>(
        sendCalendarEventEmailNotificationUrl(slug), newEvent)
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
