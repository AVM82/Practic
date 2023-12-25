import {Injectable} from '@angular/core';
import {Course} from "../models/course";
import {HttpClient} from "@angular/common/http";
import {Observable, map, of} from "rxjs";
import {Router} from "@angular/router";
import {ApiUrls, getChaptersUrl, getLevelsUrl, getMaterialsUrl, getStudentAdditionalMaterialUrl, getChapterUrl, getStudentChapterUrl, getStudentChaptersUrl, getStudentsAllAdditionalMaterialsUrl} from "../enums/api-urls";
import {AdditionalMaterials} from 'src/app/models/additional.material';
import {Level} from "../models/level";
import { Chapter, CompleteChapter } from 'src/app/models/chapter';
import { StateStudent } from '../models/student';
import { TokenStorageService } from './token-storage.service';
import { StateMentor } from '../models/mentor';
import { SvgIconRegistryService } from 'angular-svg-icon';
import { User } from '../models/user';
import { LEVEL_COLORS, STATE_NOT_STARTED } from '../enums/app-constans';
import { Practice } from '../models/practice';
import { ChapterPart } from '../models/chapterpart';
import { ReportService } from './report.service';

const requestTextResponse: Object = {
  responseType: 'text'
}

@Injectable({
  providedIn: 'root'
})
export class CoursesService {
  me!: User;
  courses?: Course[] ;
  slug: string = '';
  selectedCourse?: Course;
  levels?: Level[];
  chapters?: Chapter[];
  materials?: AdditionalMaterials[];
  stateStudent?: StateStudent;

  constructor(
    private tokenStorageService: TokenStorageService,
    private reportService: ReportService,
    private http: HttpClient,
    private _router: Router,
    private svg_registry: SvgIconRegistryService
  ) {
    this.me = tokenStorageService.getMe();
  }

  getAllCourses(): Observable<Course[]> {
    return this.courses ? of(this.courses) : this.http.get<Course[]>(ApiUrls.Courses).pipe(map(courses => {
      if (courses) {
        this.courses = courses;
        courses.forEach(course => this.svg_registry.addSvg(course.slug, course.svg));
      }
      return courses;
    }));
  }

  clearCourse(slug: string): void {
    if (this.slug === slug) {
      this.chapters = undefined;
      this.materials = undefined;
      this.stateStudent = this.tokenStorageService.me!.getStudent(slug);
    }
  }

  setCourse(slug: string): void {
    if (slug !== this.slug) {
      this.slug = slug;
      this.chapters = undefined;
      this.materials = undefined;
      this.stateStudent = this.tokenStorageService.me!.getStudent(slug);
      this.levels = undefined;
      this.selectedCourse = undefined;
    }
  }

  getCourse(slug: string): Observable<Course | undefined> {
    if (this.selectedCourse && this.slug === slug)
      return  of(this.selectedCourse);
    this.setCourse(slug);
    this.selectedCourse = this.courses?.find(course => course.slug === slug);
    return this.selectedCourse 
        ? of(this.selectedCourse) 
        : this.http.get<Course>(ApiUrls.Course + slug).pipe(map(course => {
              this.selectedCourse = course;
              return this.selectedCourse;
            }));
  }

  getChapters(slug: string): Observable<Chapter[]> {
    this.setCourse(slug);
    return this.chapters 
            ? of(this.chapters)
            : this.http.get<Chapter[]>(this.selectChaptersEndpoint(slug)).pipe(map(chapters => {
                        if (chapters) {
                          this.chapters = [];
                          chapters.forEach(chapter => this.chapters?.push(chapter));
                        }
                        return chapters;
                      }));
  }

  selectChaptersEndpoint(slug: string): string {
    if (this.stateStudent)
      return getStudentChaptersUrl(this.stateStudent.id);
    return this.me.isMentor(slug) ? ApiUrls.Mentors + `chapters/` + slug : getChaptersUrl(slug); 
  }

  extChapter(chapter: Chapter): Observable<Chapter> {
    return chapter.name ? of(this.freshChapterReports(chapter))
        : this.http.get<CompleteChapter>(this.selectChapterEndpoint(chapter))
          .pipe(map(ext => {
              Chapter.complete(chapter, ext);
              return chapter;
            }));
  }

  freshChapterReports(chapter: Chapter): Chapter {
    this.reportService.refreshChapter(chapter, this.slug);
    return chapter;
  }

  selectChapterEndpoint(chapter: Chapter): string {
    if (this.stateStudent)
      return chapter.myReports ? ApiUrls.StudentChapters + 'chapter/' + chapter.id
        : ApiUrls.StudentChapters + this.stateStudent.id + '/' + chapter.number;
    return this.me.isMentor(this.slug) ? ApiUrls.Mentors + 'chapters/' + this.slug + '/' + chapter.number
      : ApiUrls.Chapters + chapter.id; 
  }

  getLevels(slug:string): Observable<Level[]>{
    this.setCourse(slug);
    return this.levels ? of(this.levels) : this.http.get<Level[]>(getLevelsUrl(slug)).pipe(map(levels => {
        this.levels = levels;
        return levels;
    }));
  }

  getAdditionalMaterials(slug: string): Observable<AdditionalMaterials[]> {
    this.setCourse(slug);
    return this.materials ? of(this.materials)
        : this.http.get<AdditionalMaterials[]>(this.selectAdditionalMaterialsEndpoint(slug))
            .pipe(map(materials => {
                this.materials = materials;
                return materials;
              }));
  }

  selectAdditionalMaterialsEndpoint(slug: string): string {
    return this.stateStudent
      ? getStudentsAllAdditionalMaterialsUrl(this.stateStudent.id)
      : getMaterialsUrl(slug);
  }

  putAdditionalChange(event: any): void {
    this.http.put<boolean>(getStudentAdditionalMaterialUrl(this.stateStudent!.id, event.target.id), event.target.checked)
            .subscribe(ok => {
                if (ok)
                  this.materials!.find(material => material.id === event.target.id)!.checked = event.target.checked;
                else
                  event.target.checked = !event.target.checked;
            });
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
    return this.http.post<Course>(ApiUrls.NewCourse, course)
            .pipe(map(course => this.addCourse(course)));
  }

  postCourseProperties(properties: string): Observable<Course> {
    return this.http.post<Course>(ApiUrls.NewCourseFromProperties, properties)
            .pipe(map(course => this.addCourse(course)));
  }

  private addCourse(course: Course): Course {
    if (!this.courses)
      this.courses = [];
    this.courses.push(course);
    return course;
  }
  
  private getChapterByNumber(number:number): Chapter {
    return this.chapters?.find(chapter => chapter.number === number)!;
  }

  openStudentChapter(number: number): void {
    let shortChapter = this.getChapterByNumber(number);
    shortChapter.hidden = false;
    shortChapter.state = STATE_NOT_STARTED;
    shortChapter.parts = [];
    shortChapter.parts.push(new ChapterPart(new Practice(0, 0, STATE_NOT_STARTED)));
  }

  changeChapterState(number: number, state: string): void {
    this.getChapterByNumber(number).state = state;
  }

  changePracticeState(chapterN: number, practice: Practice): void {
    let shortChapter = this.getChapterByNumber(chapterN);
    shortChapter.parts.find(part => practice.number === part.practice.number)!.practice.state = practice.state;
  }

  getChapterColor(number: number): string {
    if (this.levels)
      for(let i = 0; i < this.levels.length; i++)
        if (this.levels[i].chapterN.some(chapterN => chapterN === number))
          return LEVEL_COLORS[i];
    return '#FF0000';
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

}
