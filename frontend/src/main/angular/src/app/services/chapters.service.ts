import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {Router} from "@angular/router";
import { Chapter } from '../models/chapter';
import { ApiUrls, getChapterUrl } from '../enums/api-urls';

@Injectable({
  providedIn: 'root'
})
export class ChaptersService {
  
  constructor(private http: HttpClient, private router: Router) {}

  getChapter(slug: string, chapterN: number): Observable<Chapter> {
    return this.http.get<Chapter>(getChapterUrl(slug, chapterN)).pipe(
      catchError(this.handleError<Chapter>(`getChapter chapterId=${chapterN}`))
    );
  }

  setPracticeState(state: string, chapterPartId: number): Observable<any> {
    return this.http.post(ApiUrls.PracticeStates, {state, chapterPartId});
  }

  getMyPractices(): Observable<any> {
    return this.http.get(ApiUrls.MyPractices);
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
