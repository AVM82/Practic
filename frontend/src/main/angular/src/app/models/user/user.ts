import { Applicant } from "./applicant";
import { Mentor } from "./mentor";
import { Student } from "./student";

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
      readonly roles?: string[],
      readonly students?: Student[],
      readonly mentors?: Mentor[],
      readonly applicants?: Applicant[]
  ) {}

  get isAuthenticated(): boolean {
    return this.roles?.length != 0;
  }

  hasAnyRole(...roles: string[]): boolean {
    return roles.some(roleName => this.roles?.some(userRole => userRole === roleName));
  }

  hasGuestRole(): boolean {
    return this.hasAnyRole('GUEST');
  }

  hasStudentRole(): boolean {
    return this.hasAnyRole('STUDENT');
  }

  hasMentorRole(): boolean {
    return this.hasAnyRole('MENTOR');
  }

  hasCollaboratorRole() : boolean {
    return this.hasAnyRole('COLLABORATOR');
  }

  hasAdminRole(): boolean {
    return this.hasAnyRole('ADMIN');
  }

  isStudent(slug:string): boolean {
    return this.hasStudentRole() && this.students != null && this.students.some(student => student.slug === slug);
  }

  isMentor(slug: string): boolean {
    return this.hasMentorRole() && this.mentors != null && this.mentors.some(mentor => mentor.slug === slug);
  }

  isApplicant(slug: string): boolean {
    return this.applicants != null && this.applicants.some(applicant => applicant.slug === slug);
  }

  getCourseActiveChapterNumber(slug: string): number {
    if (this.students != null)
      for (let student of this.students)
        if (student.slug === slug)
          return student.activeChapterNumber;
    return 0;
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
        json.students,
        json.mentors,
        json.applicants
    );
  }

}
