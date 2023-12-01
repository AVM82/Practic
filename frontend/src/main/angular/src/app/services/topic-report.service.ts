import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import {ApiUrls} from "../enums/api-urls";

@Injectable({
  providedIn: 'root'
})
export class TopicReportService {

  constructor(private http: HttpClient) { }

  getTopicsReportsOnChapter(studentChapterId:number): Observable<any> {
    const apiUrl = `${ApiUrls.TopicsReports}${"/"+studentChapterId}`;
    return this.http.get(apiUrl);
  }

  getStudentChapterTopicsReports(studentChapterId:number): Observable<any> {
    const apiUrl = `${ApiUrls.StudentChapterTopicsReports}${studentChapterId}`;
    return this.http.get(apiUrl);
  }
}
