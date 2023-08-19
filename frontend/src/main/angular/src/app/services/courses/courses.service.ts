import { Injectable } from '@angular/core';
import {Course} from "../../models/course/course";
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {Chapter} from "../../models/course/chapter";
import {Router} from "@angular/router";
import {ApiUrls, getChapterByIdUrl} from "../../enums/api-urls";
import {SlugifyPipe} from "../../pipes/slugify.pipe"

@Injectable({
  providedIn: 'root'
})
export class CoursesService {

  constructor(
      private http: HttpClient,
      private router: Router,
      private slugify: SlugifyPipe
      ) {}

  getCourse(slug: string): Observable<Course> {
    const shortName = this.slugify.transform(slug, false);
    return this.http.get<Course>(ApiUrls.Course+shortName).pipe(
        catchError(this.handleError<Course>(`getCourse name=${slug}`))
    );
  }

  getChapters(id: number): Observable<Chapter[]> {
    return this.http.get<Chapter[]>(getChapterByIdUrl(id));
  }

  setFirstChapterVisible(chapters: Chapter[]): void {
    if (chapters !==null && chapters.length > 0) {
      chapters[0].isVisible = true;
    }
  }

  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(ApiUrls.Courses);
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
