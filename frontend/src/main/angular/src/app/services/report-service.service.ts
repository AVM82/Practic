import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {ApiUrls, deleteReportsUrl, getReportsUrl} from "../enums/api-urls";
import {StudentReport} from "../models/studentReport";
import {NewStudentReport} from "../models/newStudentReport";
import {Router} from "@angular/router";
import { httpOptions } from '../enums/app-constans';


@Injectable({
  providedIn: 'root'
})
export class ReportServiceService {

  constructor(private http: HttpClient,
              private router: Router) {
  }

  getAllActualReports(slug: string): Observable<any[]>{
    return this.http.get<StudentReport[][]>(getReportsUrl(slug)).pipe(
        catchError(this.handleError<StudentReport[][]>(`get actual reports = ${slug}`)));
  }
  getReportStates(): Observable<any[]>{
    return this.http.get<any[]>(ApiUrls.ReportStates);
  }
  createNewReport(newReport: NewStudentReport,slug: string):Observable<any> {
    return this.http.post<NewStudentReport>(getReportsUrl(slug), JSON.stringify(newReport), httpOptions)
    .pipe(catchError(this.handleError<StudentReport>(`post new student report = ${slug}`)));

  }
  updateReportLikeList(reportId: number): Observable<any> {
    console.log("updateReportLikeList")
    return this.http.put<any>(ApiUrls.ReportLikeList, reportId, httpOptions).pipe(
        catchError(this.handleError<any>(`update report like list`)));
  }
  updateReport(newReport: NewStudentReport):Observable<any>{
    return this.http.put<NewStudentReport>(ApiUrls.Reports, JSON.stringify(newReport), httpOptions)
        .pipe(catchError(this.handleError<StudentReport>(`update new student report`)));
  }
  deleteReport(reportId:number):Observable<any>{
    return this.http.delete<NewStudentReport>(deleteReportsUrl(reportId), httpOptions)
        .pipe(catchError(this.handleError<StudentReport>(`delete new student report `)));
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error);
      console.error(`${operation} failed: ${error.message}`);

      this.router.navigateByUrl('/404');

      return of(result as T);
    };
  }
}
