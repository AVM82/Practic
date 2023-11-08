import {Injectable} from '@angular/core';
import {Course} from "../models/course";
import {HttpClient} from "@angular/common/http";
import {Observable, map, of} from "rxjs";
import {Router} from "@angular/router";
import {ApiUrls, getChaptersUrl, getLevelsUrl, getMaterialsUrl, getStudentAdditionalMaterialUrl, getChapterUrl, getStudentChapterUrl, getStudentChaptersUrl, getStudentsAllAdditionalMaterialsUrl} from "../enums/api-urls";
import {AdditionalMaterials} from 'src/app/models/additional.material';
import {Level} from "../models/level";
import { ShortChapter, Chapter } from 'src/app/models/chapter';
import { StateStudent } from '../models/student';
import { TokenStorageService } from './token-storage.service';
import { StateMentor } from '../models/mentor';
import { SvgIconRegistryService } from 'angular-svg-icon';

const requestTextResponse: Object = {
  responseType: 'text'
}

@Injectable({
  providedIn: 'root'
})
export class CoursesService {
  courses?: Course[];
  slug: string = '';
  selectedCourse?: Course;
  levels?: Level[];
  shortChapters?: ShortChapter[];
  chapters?: Chapter[];
  materials?: AdditionalMaterials[];
  currentChapter?: Chapter;
  stateStudent?: StateStudent;
  stateMentor?: StateMentor;

  constructor(
    private tokenStorageService: TokenStorageService,
    private http: HttpClient,
    private _router: Router,
    private svg_registry: SvgIconRegistryService
  ) {}

  getAllCourses(): Observable<Course[]> {
    return this.courses ? of(this.courses) : this.http.get<Course[]>(ApiUrls.Courses).pipe(map(courses => {
      this.courses = courses;
      courses.forEach(course => this.svg_registry.addSvg(course.slug, course.svg));
      return courses;
    }));
  }

  setCourse(slug: string): void {
    if (slug !== this.slug) {
      console.log(this.slug, ' -> ', slug);
      this.slug = slug;
      this.chapters = [];
      this.shortChapters = undefined;
      this.currentChapter = undefined;
      this.materials = undefined;
      this.stateStudent = this.tokenStorageService.me!.getStudent(slug);
      this.stateMentor = this.tokenStorageService.me!.getMentor(slug);
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

  getChapters(slug: string): Observable<ShortChapter[]> {
    this.setCourse(slug);
    return this.shortChapters 
            ? of(this.shortChapters)
            : this.http.get<ShortChapter[]>(this.selectChapterEndpoint(slug)).pipe(map(chapters => {
                        this.shortChapters = chapters;
                        return chapters;
                      }));
  }

  selectChapterEndpoint(slug: string): string {
    return this.stateStudent
      ? getStudentChaptersUrl(this.stateStudent.id)
      : getChaptersUrl(slug); 
  }

  getChapter(slug: string, number: number): Observable<Chapter> {
    this.setCourse(slug);
    if (this.chapters)
      for(let loadedChapter of this.chapters)
        if (loadedChapter.number === number) {
          this.currentChapter = loadedChapter;
          return of(this.currentChapter);
        }
    return this.http.get<Chapter>(this.stateStudent 
                  ? getStudentChapterUrl(this.stateStudent.id, number) 
                  : getChapterUrl(slug, number))
        .pipe(map(loadedChapter => {
            this.currentChapter = loadedChapter;
            this.chapters = [];
            this.chapters.push(loadedChapter);
            return loadedChapter;
          })
        );
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


  // temporary  STUB !!!
  getOpenChapters(): Observable<ShortChapter[]> {
    return of<ShortChapter[]>();
  }

  openChapter(studentId: number, chapterId: number): Observable<Chapter> {
    return of();
  }

  approvePractice(studentId: number, chapterPartId: number): Observable<any> {
    return this.http.post(ApiUrls.PracticeApprove, {studentId, chapterPartId});
  }


}
