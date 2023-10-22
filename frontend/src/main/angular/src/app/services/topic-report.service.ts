import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ApiUrls} from "../enums/api-urls";

@Injectable({
  providedIn: 'root'
})
export class TopicReportService {

  constructor(private http: HttpClient) { }

  getTopicsReportsOnChapter(chapter:number): Observable<any> {
    const apiUrl = `${ApiUrls.TopicsReports}${"/"+chapter}`;
    return this.http.get(apiUrl);
  }
}
