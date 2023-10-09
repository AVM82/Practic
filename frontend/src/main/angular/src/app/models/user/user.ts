
import { Course } from "../course/course";
import { UserRole } from "./user.role";

export class User {

  constructor(
      readonly id: number,
      readonly inactive: boolean,
      readonly ban: boolean,
      readonly email: string,
      readonly name: string,
      readonly discord: string | null,
      readonly linkedin: string,
      readonly contacts: string | null,
      readonly profilePictureUrl: string,
      readonly applyCourse: string,
      readonly roles: UserRole[],
      readonly courses: Course[]
  ) {}

  get isAuthenticated(): boolean {
    return this.roles.length != 0;
  }

  hasAnyRole(...roles: string[]): boolean {
    return roles.some(roleName => this.roles.some(userRole => userRole.name === roleName));
  }

  hasApplyOnCourse(...courses: string[]): boolean {
   return courses.some(courseSlug => this.courses.some(course => course.slug === courseSlug));
  }

  static fromJson(json: any): User {
    return new User(
        json.id,
        json.inactive,
        json.ban,
        json.email,
        json.name,
        json.discord,
        json.linkedin,
        json.contacts,
        json.profilePictureUrl,
        json.applyCourse,
        json.roles,
        json.courses
    );
  }

}
