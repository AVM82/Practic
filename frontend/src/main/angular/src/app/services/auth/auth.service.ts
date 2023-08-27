import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {ApiUrls} from "../../enums/api-urls";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
      private http: HttpClient
  ) { }

  getCurrentUser(): Observable<any> {
    return this.http.get(ApiUrls.Me, httpOptions);
  }
}

export class User {
  static readonly ANONYMOUS = new User('', '', []);

  constructor(
      readonly subject: string,
      readonly issuer: string,
      readonly roles: string[]
  ) {}

  get isAuthenticated(): boolean {
    return !!this.subject;
  }

  hasAnyRole(...roles: string[]): boolean {
    for (let r in roles) {
      if (this.roles.includes(r)) {
        return true;
      }
    }
    return false;
  }
}
