import { Injectable } from '@angular/core';
import {Practice} from "../../models/practice/practice";
import { User } from 'src/app/models/user/user';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';
const PRACTICE_KEY = 'practice-data';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  
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
    return userJson ? User.fromJson(JSON.parse(userJson)) : null;
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
    const token = this.getToken();
    return token ? this.getUser() : null;
  }

  isStudent(slug: string): boolean {
    const me = this.getMe();
    return me?.hasAnyRole('STUDENT') && me.hasApplyOnCourse(slug);
  }


  public haveIAnyRole(roles: string[]): boolean {
    const me = this.getMe();
    return me?.hasAnyRole(...roles);
  }
}
