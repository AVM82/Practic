import { Injectable } from '@angular/core';
import {Practice} from "../../models/practice/practice";
import { User } from 'src/app/models/user/user';
import { CoursesService } from '../courses/courses.service';
import { ActivatedRoute } from '@angular/router';
import { AdvancedRoles } from 'src/app/enums/roles.enum';
import { Observable, map, of } from 'rxjs';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';
const PRACTICE_KEY = 'practice-data';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  
  constructor(
    private coursesService: CoursesService,
    private route: ActivatedRoute  
  ) {  }

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

  public isStudent(slug: string): boolean {
    const me = this.getMe();
    return me ? me.hasApplyOnCourse(slug) : false;
  }

  public isAnyAdvancedRole(slug: string,): Observable<boolean> {
    if (this.haveIAnyRole(AdvancedRoles))
      return of(true);
    return this.isMentor(slug, true);
  }

  public isMentor(slug: string, such: boolean): Observable<boolean> {
    const me = this.getMe();
    return me && this.coursesService.getMentors(slug).pipe(
                    map<User[], boolean>(mentors => {
                      return (mentors != null && mentors.some(mentor => mentor.id === me.id)) == such;
                    })
                );
  }

  public neitherStudentNorMentor(slug: string): Observable<boolean> {
    return this.isStudent(slug) ? of(false) : this.isMentor(slug, false);
  }

  public haveIAnyRole(roles: string[]): boolean {
    const me = this.getMe();
    return me ? me.hasAnyRole(...roles) : false;
  }
}
