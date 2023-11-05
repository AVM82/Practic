import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import { Course } from '../models/course';
import { ApiUrls } from '../enums/api-urls';

@Injectable({
  providedIn: 'root'
})
export class StudentMetricsService {

  constructor(private http: HttpClient) {}

  getAllPracticesByState(state: string): Observable<any[]>{
    return this.http.get<Course[]>(ApiUrls.Practices + state);
  }

  getPracticeStates(): Observable<any[]>{
    return this.http.get<any[]>(ApiUrls.PracticeStates);
  }

  getApplicants(): Observable<any[]>{
    return this.http.get<any[]>(ApiUrls.Application);
  }
}
