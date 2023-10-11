import {Injectable} from '@angular/core';
import {Course} from "../../models/course/course";
import {HttpClient} from "@angular/common/http";
import {map, Observable, of} from "rxjs";
import {Chapter} from "../../models/course/chapter";
import {Router} from "@angular/router";
import {ApiUrls, getCourseUrl, getChaptersUrl, getLevelsUrl, getMaterialsUrl, getStudentAdditionalMaterialUrl, getMaterialsExistUrl, getActiveChapterNumber} from "../../enums/api-urls";
import {AdditionalMaterials} from 'src/app/models/material/additional.material';
import {Level} from "../../models/level/level";
import { User } from 'src/app/models/user/user';

const requestTextResponse: Object = {
  responseType: 'text'
}

@Injectable({
  providedIn: 'root'
})
export class CoursesService {
  selectedCourse!: Observable<Course>;
  levels: Observable<Level[]> | undefined;
  chapters: Observable<Chapter[]> | undefined;
  slug: string = '';

  constructor(
      private http: HttpClient,
      private _router: Router
      ) {}

  setCourse(slug: string): void {
    if (slug !== this.slug) {
      console.log(this.slug, ' -> ', slug);
      this.slug = slug;
      this.selectedCourse = this.http.get<Course>(getCourseUrl(slug));
      this.chapters = undefined;
      this.levels = undefined;
        console.log('this course : ', this.selectedCourse);
        console.log('this levels : ', this.levels);
        console.log('this chapters : ', this.chapters);
    }
  }

  getCourse(slug: string): Observable<Course> {
    this.setCourse(slug);
    return this.selectedCourse;
  }


  getChapters(slug: string): Observable<Chapter[]> {
    this.setCourse(slug);
    if (!this.chapters)
      this.chapters = this.http.get<Chapter[]>(getChaptersUrl(slug));
    return this.chapters;
  }

  getLevels(slug:string):Observable<Level[]>{
    this.setCourse(slug);
    if (!this.levels)
      this.levels = this.http.get<Level[]>(getLevelsUrl(slug));
    return this.levels;
  }

  openChapter(studentId: number, chapterId: number): Observable<any> {
    return this.http.post<any>("/api/students/chapters", {studentId, chapterId});
  }

  getOpenChapters(): Observable<Chapter[]> {
    return this.http.get<Chapter[]>(ApiUrls.OpenChapters);
  }

  setFirstChapterVisible(chapters: Chapter[]): void {
    if (chapters !==null && chapters.length > 1) {
      chapters[0].visible = true;
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
          chapter.visible = true;
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

  getAdditionalMaterialsExist(slug: string): Observable<boolean> {
    return this.http.get<boolean>(getMaterialsExistUrl(slug));
  }

  postCourseInteractive(_slug: any,  _name: any, _svg: any): Observable<Course> {
    let course: Course = {
      id: 0,
      mentors: [],
      slug: _slug,
      name: _name,
      svg: _svg
    };
    return this.http.post<Course>(ApiUrls.Courses, course);
  }

  postCourseProperties(properties: string): Observable<Course> {
    return this.http.post<Course>(ApiUrls.NewCourseFromProperties, properties);
  }

  putAdditionalChange(slug: string, id: number, checked: boolean): Observable<boolean> {
    return this.http.put<boolean>(getStudentAdditionalMaterialUrl(slug, id), checked);
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

  getDescription(slug: string): Observable<any> {
    this.setCourse(slug);
    return this.selectedCourse.pipe(map (course => course.description));
  }

  getMentors(slug: string): Observable<User[]> {
    this.setCourse(slug);
    return this.selectedCourse.pipe(map(course => course.mentors));
  }

  getActiveChapterNumber(slug: string): any {
    this.http.get<number>(getActiveChapterNumber(slug)).subscribe(response => {
      return response });
  }

}
