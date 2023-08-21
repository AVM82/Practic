import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {Chapter} from "../../models/chapter/chapter";
import {Router} from "@angular/router";
import {getChapterUrl} from "../../enums/api-urls";

@Injectable({
  providedIn: 'root'
})
export class ChaptersService {
  
  constructor(private http: HttpClient, private router: Router) {}

  getChapter(courseId: number, chapterId: number): Observable<Chapter> {
     return this.http.get<Chapter>(getChapterUrl(courseId, chapterId)).pipe(
        catchError(this.handleError<Chapter>(`getChapter chapterId=${chapterId}`))
    );
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
