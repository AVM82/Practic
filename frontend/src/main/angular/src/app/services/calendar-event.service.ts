import { Injectable } from '@angular/core';
import {NewStudentReport} from "../models/newStudentReport";
import {catchError, Observable, of} from "rxjs";
import { sendCalendarEventEmailNotificationUrl} from "../enums/api-urls";
import {Report} from "../models/report";
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
        .pipe(catchError(this.handleError<Report>(`send notification of creating report = `)));

  }
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error);
      console.error(`${operation} failed: ${error.message}`);

      this.router.navigateByUrl('/404');

      return of(result as T);
    };
  }

  static toMs(date: Date): number {
    return date ? Math.trunc(date.valueOf() + date.getTimezoneOffset()*60000) : 0;
  }
  
  static stringDateToLocalDate(date: string): Date {
    return this.toLocalDate(new Date(date));
  }

  static toLocalDate(date: Date): Date {
    return new Date(date.getTime() - date.getTimezoneOffset()*60000)
  }

  static dateFromNowPlus(days: number): Date {
    const date = new Date(Math.trunc(new Date().getTime() / 86400000) * 86400000);
    console.log('dateFromNowPlus-> now-',date);
    date.setDate(date.getDate() + days);
    return date;
  }

}
function tolLocalDate(arg0: Date): Date {
  throw new Error('Function not implemented.');
}

