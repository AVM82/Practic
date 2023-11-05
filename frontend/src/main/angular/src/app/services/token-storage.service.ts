import { Injectable } from '@angular/core';
import { environment } from 'src/enviroments/enviroment';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient } from '@angular/common/http';
import { ApiUrls } from 'src/app/enums/api-urls';
import { User } from '../models/user';
import { httpOptions } from '../enums/app-constans';
import { Practice } from '../models/practice';

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

  public saveUser(user: User): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): User {
    const userJson = window.sessionStorage.getItem(USER_KEY);
    this.me = userJson ? User.fromJson(JSON.parse(userJson)) : undefined;
    return this.me!;
  }

  public refreshMe(): void {
    this.getCurrentUser().subscribe(user => {
      this.me!.update(user);
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

  public updateMe(user: User): void {
    this.saveUser(this.me!.update(user));
  }
  
}
