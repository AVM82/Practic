import { Injectable } from '@angular/core';
import {User} from "./auth.service";

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

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

  public isStudent(slug: string): boolean {
    const token = this.getToken();
    if (token) {
      const user: User = this.getUser();
      return user?.hasApplyOnCourse(slug);
    }
    return false;
  }

  public haveIAnyRole(...roles: string[]): boolean {
    const token = this.getToken();
    if (token) {
      const user: User = this.getUser();
      return user.hasAnyRole(...roles);
    }
    return false;
  }
}
