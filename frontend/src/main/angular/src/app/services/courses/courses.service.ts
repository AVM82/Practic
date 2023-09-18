import {Injectable} from '@angular/core';
import {Course} from "../../models/course/course";
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {Chapter} from "../../models/course/chapter";
import {Router} from "@angular/router";
import {ApiUrls, getChaptersUrl, getLevelsUrl, getMaterialsUrl} from "../../enums/api-urls";
import {AdditionalMaterials} from 'src/app/models/material/additional.material';
import {Level} from "../../models/level/level";

@Injectable({
  providedIn: 'root'
})
export class CoursesService {

  constructor(
      private http: HttpClient,
      private _router: Router
      ) {}

  getCourse(slug: string): Observable<Course> {
    return this.http.get<Course>(ApiUrls.Course+slug).pipe(
        catchError(this.handleError<Course>(`get course = ${slug}`))
    )
  }

  getChapters(slug: string): Observable<Chapter[]> {
    return this.http.get<Chapter[]>(getChaptersUrl(slug));
  }
  getLevels(slug:string):Observable<Level[]>{
    return this.http.get<Level[]>(getLevelsUrl(slug))
  }

  setFirstChapterVisible(chapters: Chapter[]): void {
    if (chapters !==null && chapters.length > 1) {
      chapters[0].isVisible = true;
      chapters[1].isVisible = true;
    }
  }

  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(ApiUrls.Courses);
  }

  getAdditionalMaterials(slug: string): Observable<AdditionalMaterials[]> {
    return this.http.get<AdditionalMaterials[]>(getMaterialsUrl(slug));
  }

  postAdditionalChange(slug: string, id: number, checked: boolean) {

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

      this._router.navigateByUrl('/404');
      return of(result as T);
    };
  }

  setActiveChapter(chapters: Chapter[], chapterId: number) {
    if (chapters !== null
        && chapters.length > 0
        && chapters.some(chapter => chapter.id === chapterId)
    ) {
      this.resetAllChapters(chapters);
      chapters.forEach(chapter => {
        chapter.isActive = chapter.id === chapterId;
      });
    }
  }

  resetAllChapters(chapters: Chapter[]) {
    chapters.forEach(chapter => {
      chapter.isActive = false;
    });
  }
}
