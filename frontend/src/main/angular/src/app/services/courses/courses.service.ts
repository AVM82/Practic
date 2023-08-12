import { Injectable } from '@angular/core';
import {Course} from "../../models/course/course";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Chapter} from "../../models/course/chapter";

@Injectable({
  providedIn: 'root'
})
export class CoursesService {

  constructor(private http: HttpClient) {}

  getCourse(id: number): Observable<Course> {
    return this.http.get<Course>("/api/course/"+id)
  }

  getChapters(id: number): Observable<Chapter[]> {
    return this.http.get<Chapter[]>("/api/course/"+id+"/chapters")
  }

  setFirstChapterVisible(chapters: Chapter[]): void {
    if (chapters.length > 0) {
      chapters[0].isVisible = true;
    }
  }

  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>("/api/course");
  }
}
