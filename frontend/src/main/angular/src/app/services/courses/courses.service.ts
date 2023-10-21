import {Injectable} from '@angular/core';
import {Course} from "../../models/course/course";
import {HttpClient} from "@angular/common/http";
import {map, Observable, of} from "rxjs";
import {Router} from "@angular/router";
import {ApiUrls, getChaptersUrl, getLevelsUrl, getMaterialsUrl, getStudentAdditionalMaterialUrl, getActiveChapterNumber, getCourseUrl, getChapterUrl, getApplicationUrl, getStudentsAllAdditionalMaterialsUrl} from "../../enums/api-urls";
import {AdditionalMaterials} from 'src/app/models/material/additional.material';
import {Level} from "../../models/level/level";
import { User } from 'src/app/models/user/user';
import { Chapter } from 'src/app/models/chapter/chapter';
import { TokenStorageService } from '../auth/token-storage.service';
import { AdvancedRoles } from 'src/app/enums/roles.enum';
import { ShortChapter } from 'src/app/models/course/chapter';

const requestTextResponse: Object = {
  responseType: 'text'
}

@Injectable({
  providedIn: 'root'
})
export class CoursesService {
  slug: string = '';
  selectedCourse!: Observable<Course>;
  levels!: Observable<Level[]>;
  shortChapters!: Observable<ShortChapter[]>;
  chapters: Chapter[] = [];
  me: User;
  id: number = 0;
  isStudent: boolean = false;

  constructor(
    private tokenStorageService: TokenStorageService,
    private http: HttpClient,
    private _router: Router
  ) {
    this.me = tokenStorageService.getMe();
    if (this.me)
      this.id = this.me.id;
  }

  setCourse(slug: string): void {
    if (slug !== this.slug) {
      console.log(this.slug, ' -> ', slug);
      this.slug = slug;
      this.chapters = [];
      this.shortChapters = this.http.get<ShortChapter[]>(getChaptersUrl(slug));
      this.levels = this.http.get<Level[]>(getLevelsUrl(slug));
      this.isStudent = this.tokenStorageService.isStudent(slug);
      this.levels = this.http.get<Level[]>(getLevelsUrl(slug));
      this.selectedCourse = this.http.get<Course>(getCourseUrl(slug));
    }
  }

  public isAnyAdvancedRole(slug: string): boolean {
    this.setCourse(slug);
    return this.tokenStorageService.haveIAnyRole(AdvancedRoles);
  }

  getCourse(slug: string): Observable<Course> {
    this.setCourse(slug);
    return this.selectedCourse;
  }


  getChapters(slug: string): Observable<ShortChapter[]> {
    this.setCourse(slug);
    return this.shortChapters;
  }

  loadChapter(slug: string, number: number): Observable<Chapter> {
    this.setCourse(slug);
    for(let loadedChapter of this.chapters)
      if (loadedChapter.number === number) 
        return of(loadedChapter);
    return this.http.get<Chapter>(getChapterUrl(slug,number)).pipe(map<Chapter, Chapter> (loadedChapter => {
      this.chapters.push(loadedChapter);
      return loadedChapter;
    }))
  }

  getLevels(slug:string):Observable<Level[]>{
    this.setCourse(slug);
    return this.levels;
  }

  openChapter(studentId: number, chapterId: number): Observable<any> {
    return this.http.post<any>("/api/students/chapters", {studentId, chapterId});
  }

  getOpenChapters(): Observable<Chapter[]> {
    return this.http.get<Chapter[]>(ApiUrls.OpenChapters);
  }

  setFirstChapterVisible(chapters: ShortChapter[]): void {
    if (chapters?.length > 1) {
      chapters[0].visible = true;
    }
  }

  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(ApiUrls.Courses);
  }

  isAppliedOnCourse(slug: string): Observable<boolean> {
    return this.http.get<boolean>(getApplicationUrl(slug));
  }

  confirmApplyOnCourse(courseSlug: string, userId: number): Observable<any> {
    return this.http.post(ApiUrls.Students + '/apply', {courseSlug, userId});
  }

  approvePractice(studentId: number, chapterPartId: number): Observable<any> {
    return this.http.post(ApiUrls.PracticeApprove, {studentId, chapterPartId});
  }

  getAdditionalMaterials(slug: string): Observable<AdditionalMaterials[]> {
    this.setCourse(slug);
    return this.http.get<AdditionalMaterials[]>(this.isStudent ?  getStudentsAllAdditionalMaterialsUrl(slug) : getMaterialsUrl(slug));
  }

  postCourseInteractive(_slug: string,  _name: string, _svg: string): Observable<Course> {
    let course: Course = {
      id: 0,
      mentors: [],
      slug: _slug,
      name: _name,
      svg: _svg,
      additionalMaterialsExist: false
    };
    return this.http.post<Course>(ApiUrls.NewCourse, course);
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

  getMentors(slug: string): Observable<User[]> {
    this.setCourse(slug);
    return this.selectedCourse.pipe(map (course => course.mentors));
  }

  amIAmongOthers(others: User[]): boolean {
    return this.id != 0 && others?.some(other => other.id === this.id);
  }

  isMentor(slug: string): Observable<boolean> {
    return this.getMentors(slug).pipe(map<User[], boolean>(
      mentors => {
        return this.amIAmongOthers(mentors);
    })); 
  }

  isNotMentor(slug: string): Observable<boolean> {
    return this.getMentors(slug).pipe(map<User[], boolean>(
      mentors => {
        return !this.amIAmongOthers(mentors);
    })); 
  }

  isUninvolved(slug: string): Observable<boolean> {
    return this.getMentors(slug).pipe(map<User[], boolean>(
      mentors => {
        return !this.isStudent && (mentors == null || !mentors.some(mentor => mentor.id === this.me.id));
    }));
  }

  getActiveChapterNumber(slug: string): Observable<number> {
    this.setCourse(slug);
    return  this.http.get<number>(getActiveChapterNumber(slug));
  }

}
