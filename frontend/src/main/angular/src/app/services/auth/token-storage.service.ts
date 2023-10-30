import { Injectable } from '@angular/core';
import {Practice} from "../../models/practice/practice";
import { User } from 'src/app/models/user/user';
import { environment } from 'src/enviroments/enviroment';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient } from '@angular/common/http';
import { httpOptions } from 'src/app/constants';
import { ApiUrls } from 'src/app/enums/api-urls';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';
const PRACTICE_KEY = 'practice-data';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  me: User | undefined;

  constructor(
    private http: HttpClient
  ) { }

  getCurrentUser(): Observable<User> {
    console.log('query current user ');
    return this.http.get<User>(ApiUrls.Me, httpOptions);
  }

  signOut(): void {
    window.sessionStorage.clear();
  }

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): any {
    const userJson = window.sessionStorage.getItem(USER_KEY);
    this.me = userJson ? User.fromJson(JSON.parse(userJson)) : undefined;
    return this.me;
  }

  public refreshUser(): void {
    this.getCurrentUser().subscribe(user => {
      this.me = user;
      this.saveUser(user);
    })
  }

  public savePractice(practice: Practice[]): void {
    window.sessionStorage.removeItem(PRACTICE_KEY);
    window.sessionStorage.setItem(PRACTICE_KEY, JSON.stringify(practice));
  }

  public getPractice(): Practice[] | null {
    const practiceJson = sessionStorage.getItem(PRACTICE_KEY);
    return practiceJson ? JSON.parse(practiceJson) : null;
  }

  public updatePractice(practice: Practice[]): void {
    this.savePractice(practice);
  }

  public getMe(): User {
    if (this.me == undefined)
      window.location.href = environment.loginBaseUrl;
    return this.me!;
  }

}
