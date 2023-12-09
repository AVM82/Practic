import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map, Observable, of} from 'rxjs';
import {ApiUrls} from "../enums/api-urls";
import {Chapter} from "../models/chapter";
import {TopicReport} from "../models/report";

@Injectable({
    providedIn: 'root'
})
export class TopicReportService {

    constructor(
        private http: HttpClient
    ) { }

    getTopicsReportsOfChapter(studentChapter: Chapter): Observable<TopicReport []> {
        return studentChapter.topicReports ? of(studentChapter.topicReports) :
            this.http.get<TopicReport []>(`${ApiUrls.StudentChapterTopicsReports}${studentChapter.id}`)
                .pipe(map(loadedTopicReports => {
                        studentChapter.topicReports = loadedTopicReports;
                        return loadedTopicReports;
                    })
                );
    }

}
