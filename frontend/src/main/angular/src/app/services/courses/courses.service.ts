import {Injectable} from '@angular/core';
import {Course} from "../../models/course/course";
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {Chapter} from "../../models/course/chapter";
import {Router} from "@angular/router";
import {ApiUrls, getCourseUrl, getChaptersUrl, getLevelsUrl, getMaterialsUrl} from "../../enums/api-urls";
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
    return this.http.get<Course>(getCourseUrl(slug)).pipe(
        catchError(this.handleError<Course>(`get course = ${slug}`))
    )
  }

  getChapters(slug: string): Observable<Chapter[]> {
    return this.http.get<Chapter[]>(getChaptersUrl(slug));
  }

  getLevels(slug:string):Observable<Level[]>{
    return this.http.get<Level[]>(getLevelsUrl(slug))
  }

  openChapter(studentId: number, chapterId: number): Observable<any> {
    return this.http.post<any>("/api/students/chapters", {studentId, chapterId});
  }

  getOpenChapters(): Observable<Chapter[]> {
    return this.http.get<Chapter[]>(ApiUrls.OpenChapters);
  }

  setFirstChapterVisible(chapters: Chapter[]): void {
    if (chapters !==null && chapters.length > 1) {
      chapters[0].isVisible = true;
    }
  }

  setVisibleChapters(chapters: Chapter[], openChapters: Chapter[]): void {
    if (chapters && openChapters) {
      const openChapterMap = new Map<number, Chapter>();

      for (const openChapter of openChapters) {
        openChapterMap.set(openChapter.id, openChapter);
      }

      for (const chapter of chapters) {
        if (openChapterMap.has(chapter.id)) {
          chapter.isVisible = true;
        }
      }
    }
  }

  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(ApiUrls.Courses);
  }

  confirmApplyOnCourse(courseSlug: string, userId: number): Observable<any> {
    return this.http.post('/api/students', {courseSlug, userId});
  }

  approvePractice(studentId: number, chapterPartId: number): Observable<any> {
    return this.http.post(ApiUrls.PracticeApprove, {studentId, chapterPartId});
  }

  getAdditionalMaterials(slug: string): Observable<AdditionalMaterials[]> {
    return this.http.get<AdditionalMaterials[]>(getMaterialsUrl(slug));
  }

  postCourseInteractive(_slug: any,  _name: any, _svg: any): Observable<Course> {
    let course: Course = {
      id: 0,
      chapters: [],
      slug: _slug,
      name: _name,
      svg: _svg
    };
    return this.http.post<Course>(ApiUrls.Courses, course);
  }

  postCourseProperties(properties: string): Observable<Course> {
    return this.http.post<Course>(ApiUrls.NewCourseFromProperties, properties);
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
    if (chapters
        && chapters.length > 0
        && chapters.some(chapter => chapter.number === chapterId)
    ) {
      this.resetAllChapters(chapters);
      chapters.forEach(chapter => {
        chapter.isActive = chapter.number === chapterId;
      });
    }
  }

  resetAllChapters(chapters: Chapter[]) {
    if (chapters) {
      chapters.forEach(chapter => {
        chapter.isActive = false;
      });
    }
  }
}
