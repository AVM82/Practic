import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map, Observable, of} from 'rxjs';
import {ApiUrls} from "../enums/api-urls";

@Injectable({
    providedIn: 'root'
})
export class TopicReportService {

    constructor(private http: HttpClient) {
    }

    topicReports: any[][] = [];


    getTopicsReportsOnChapter(studentChapterId: number): Observable<any> {
        console.log(this.topicReports)
        for (let loadedTopicReports of this.topicReports) {
            if (loadedTopicReports[0].chapter.id === studentChapterId) {
                return of(loadedTopicReports);
            }
        }
        return this.http.get<any>(`${ApiUrls.TopicsReports}${studentChapterId}`)
            .pipe(map(loadedTopicReports => {
                    this.topicReports.push(loadedTopicReports);
                    return loadedTopicReports;
                })
            );
    }


    getStudentChapterTopicsReports(studentChapterId: number): Observable<any> {
        const apiUrl = `${ApiUrls.StudentChapterTopicsReports}${studentChapterId}`;
        return this.http.get(apiUrl);
    }
}
