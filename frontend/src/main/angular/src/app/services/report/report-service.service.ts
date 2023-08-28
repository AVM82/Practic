import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Course} from "../../models/course/course";
import {ApiUrls} from "../../enums/api-urls";
import {StudentReport} from "../../models/report/studentReport";

@Injectable({
  providedIn: 'root'
})
export class ReportServiceService {

  constructor(private http: HttpClient) {
  }
  getAllActualReports(slug: string): Observable<any[]>{
    return this.http.get<StudentReport[][]>(ApiUrls.Reports + slug);
  }
  getReportStates(): Observable<any[]>{
    return this.http.get<any[]>(ApiUrls.ReportStates);
  }
}
