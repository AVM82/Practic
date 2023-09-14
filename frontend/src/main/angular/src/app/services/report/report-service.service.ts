import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {ApiUrls, getReportsUrl, postReportsUrl} from "../../enums/api-urls";
import {StudentReport} from "../../models/report/studentReport";
import {NewStudentReport} from "../../models/newStudentReport/newStudentReport";
import {Router} from "@angular/router";


@Injectable({
  providedIn: 'root'
})
export class ReportServiceService {

  constructor(private http: HttpClient,
              private router: Router) {
  }
   headers = new HttpHeaders({
    'Content-Type': 'application/json',
  });

  getAllActualReports(slug: string): Observable<any[]>{
    return this.http.get<StudentReport[][]>(getReportsUrl(slug)).pipe(
        catchError(this.handleError<StudentReport[][]>(`get actual reports = ${slug}`)));
  }
  getReportStates(): Observable<any[]>{
    return this.http.get<any[]>(ApiUrls.ReportStates);
  }
  createNewReport(newReport: NewStudentReport,slug: string):Observable<NewStudentReport> {
    console.log(JSON.stringify(newReport))
    return this.http.post<NewStudentReport>(postReportsUrl(slug), JSON.stringify(newReport),{ headers: this.headers } )
    .pipe(catchError(this.handleError<NewStudentReport>(`post new student report = ${slug}`)));

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
