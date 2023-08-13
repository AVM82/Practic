import { Injectable } from '@angular/core';
import {Course} from "../../models/course/course";
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {Chapter} from "../../models/course/chapter";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class CoursesService {
  private coursesCache: Map<number, Course> = new Map<number, Course>();
  private chaptersCache: Map<number, Chapter[]> = new Map<number, Chapter[]>();

  constructor(private http: HttpClient, private router: Router) {}

  getCourse(id: number): Observable<Course> {
    if (this.coursesCache.has(id)) {
      const cachedCourse = this.coursesCache.get(id);
      if (cachedCourse) {
        return of(cachedCourse);
      }
    }
    return this.http.get<Course>("/api/course/"+id).pipe(
        catchError(this.handleError<Course>(`getCourse id=${id}`))
    );
  }

  getChapters(id: number): Observable<Chapter[]> {
    if (this.chaptersCache.has(id)) {
      const cachedChapters = this.chaptersCache.get(id);
      if (cachedChapters) {
        return of(cachedChapters);
      }
    }
    return this.http.get<Chapter[]>("/api/course/"+id+"/chapters");
  }

  setFirstChapterVisible(chapters: Chapter[]): void {
    if (chapters !==null && chapters.length > 0) {
      chapters[0].isVisible = true;
    }
  }

  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>("/api/course");
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
