import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError,  Observable, of, tap} from "rxjs";
import {ApiUrls, deleteReportsUrl, getReportsUrl, postReportsUrl} from "../enums/api-urls";
import {Report} from "../models/report";
import {NewStudentReport} from "../models/newStudentReport";
import {Router} from "@angular/router";
import { ReportState } from '../enums/app-constans';


@Injectable({
    providedIn: 'root'
})
export class ReportServiceService {

    slug: string = '';
    reports: Report[][] = [] ;

    constructor(private http: HttpClient,
                private router: Router) {
    }

    setCourseSlug(slug: string): void {
        if (slug !== this.slug) {
            this.reports = [];
            this.slug = slug;
        }
    }

    getAllActualReports(slug: string): void {
        this.setCourseSlug(slug);
        this.http.get<Report[][]>(getReportsUrl(slug)).subscribe(reports => this.reports = reports);
    }

    createNewReport(newReport: NewStudentReport): Observable<Report> {
        return this.http.post<Report>(postReportsUrl(newReport.chapterId), newReport)
            .pipe(
                tap(report => this.reports[report.chapterNumber - 1].push(report)),
                catchError(this.handleError<Report>(`post new student report = ${newReport.chapterId}`)));
    }

    updateReportLikeList(reportId: number): Observable<any> {
        console.log("updateReportLikeList")
        return this.http.put<any>(ApiUrls.ReportLikeList, reportId).pipe(
            catchError(this.handleError<any>(`update report like list`)));
    }

    updateReport(newReport: NewStudentReport): Observable<any> {
        return this.http.put<NewStudentReport>(ApiUrls.Reports, newReport)
            .pipe(catchError(this.handleError<Report>(`update new student report`)));
    }

    deleteReport(reportId: number): Observable<any> {
        return this.http.delete<NewStudentReport>(deleteReportsUrl(reportId))
            .pipe(catchError(this.handleError<Report>(`delete new student report `)));
    }

    reportsSubmitted(reports: Report[] | undefined): boolean {
        return reports != undefined && reports?.some(report => report.state === ReportState.APPROVED);
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

    showReports(){
        console.log(this.reports)
    }
}
