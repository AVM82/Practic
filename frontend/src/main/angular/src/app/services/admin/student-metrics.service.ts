import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {Course} from "../../models/course/course";
import {ApiUrls} from "../../enums/api-urls";
import {HttpClient} from "@angular/common/http";

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
}
