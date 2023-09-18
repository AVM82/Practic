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
  static readonly ANONYMOUS = new User([],"","","",0);
  
  constructor(
      readonly roles: UserRole[],
      readonly name : string,
      readonly profilePictureUrl : string,
      readonly email : string,
      readonly id : number
  ) {}

  get isAuthenticated(): boolean {
    return this.roles.length != 0;
  }

  hasAnyRole(...roles: string[]): boolean {
    return roles.some(roleName => this.roles.some(userRole => userRole.name === roleName));
  }

}

export class UserRole {
  constructor(
      readonly id: number,
      readonly name: string
  ) {
  }
}
