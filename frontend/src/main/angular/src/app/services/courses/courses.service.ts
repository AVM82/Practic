import {Injectable} from '@angular/core';
import {Course} from "../../models/course/course";
import {HttpClient} from "@angular/common/http";
import {map, Observable, of} from "rxjs";
import {Router} from "@angular/router";
import {ApiUrls, getChaptersUrl, getLevelsUrl, getMaterialsUrl, getStudentAdditionalMaterialUrl, getMaterialsExistUrl, getActiveChapterNumber, getCourseUrl} from "../../enums/api-urls";
import {AdditionalMaterials} from 'src/app/models/material/additional.material';
import {Level} from "../../models/level/level";
import { User } from 'src/app/models/user/user';
import { Chapter } from 'src/app/models/chapter/chapter';
import { TokenStorageService } from '../auth/token-storage.service';
import { AdvancedRoles } from 'src/app/enums/roles.enum';

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
  chapters!: Observable<Chapter[]>;
  active: number = 0;
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
      this.getActiveChapterNumber(slug).subscribe(value => this.active = value);
      this.chapters = this.http.get<Chapter[]>(getChaptersUrl(slug));
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


  getChapters(slug: string): Observable<Chapter[]> {
    this.setCourse(slug);
    return this.chapters!;
  }

  getChapter(slug: string, number: number): Observable<Chapter> {
    this.setCourse(slug);
    return this.getChapters(slug).pipe(map<Chapter[], Chapter>( chapters  => {
        return chapters.find(chapter => chapter.number === number)!
    }));
  }

  setVisibleChapters(chapters: Chapter[]): void {
      chapters.forEach(chapter => 
          chapter.visible = this.active >= chapter.number
      )
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

  setFirstChapterVisible(chapters: Chapter[]): void {
    if (chapters?.length > 1) {
      chapters[0].visible = true;
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
        return !this.isStudent && (mentors == null || !mentors.some(mentor => mentor.id === this.me!.id));
    }));
  }

  getActiveChapterNumber(slug: string): Observable<any> {
    this.setCourse(slug);
    return  this.http.get<number>(getActiveChapterNumber(slug));
  }

}
