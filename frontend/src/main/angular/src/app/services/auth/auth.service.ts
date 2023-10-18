import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {ApiUrls} from "../../enums/api-urls";
import { User } from 'src/app/models/user/user';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type':'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
      private http: HttpClient
  ) { }

  getCurrentUser(): Observable<any> {
    return this.http.get<User>(ApiUrls.Me, httpOptions);
  }

  applyOnCourse(slug :string): Observable<any> {
    const url = `/api/persons/application/${slug}`;
    return this.http.post(url, {}, httpOptions);
  }
}

