import { Injectable } from '@angular/core';
import {Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class StudentMetricsService {

  getStudents(): Observable<any[]>{
    return of(studentsData);
  }
}

export const studentsData = [
  { id: 1, name: 'Oleksandr Talavas', chapterId: 1 },
  { id: 2, name: 'Student 2', chapterId: 2 },
  { id: 3, name: 'Student 3', chapterId: 1 },

];
