import { ROLE_GUEST, ROLE_MENTOR, ROLE_STUDENT, Roles } from "../enums/app-constans";
import { Applicant, StateApplicant } from "./applicant";
import { StateMentor } from "./mentor";
import { StateGraduate, StateStudent } from "./student";

export class User {
  id!: number;
  inactive!: boolean;
  ban!: boolean;
  email!: string;
  name!: string;
  discord!: string | null;
  linkedin!: string;
  contacts!: string | null;
  profilePictureUrl!: string;
  roles!: string[];
  students!: StateStudent[];
  graduates!: StateGraduate[];
  mentors!: StateMentor[];
  applicants!: StateApplicant[];
  
  constructor(
      _id: number,
      _inactive: boolean,
      _ban: boolean,
      _email: string,
      _name: string,
      _discord: string | null,
      _linkedin: string,
      _contacts: string | null,
      _profilePictureUrl: string,
      _roles: string[],
      _students: StateStudent[],
      _graduates: StateGraduate[],
      _mentors: StateMentor[],
      _applicants: StateApplicant[]
  ) {
    this.id = _id;
    this.inactive = _inactive;
    this.ban = _ban;
    this.email = _email;
    this.name =_name;
    this.discord = _discord;
    this.linkedin = _linkedin;
    this.contacts = _contacts;
    this.profilePictureUrl =_profilePictureUrl;
    this.roles = _roles;
    this.students = _students;
    this.graduates = _graduates || [];
    this.mentors =_mentors;
    this.applicants = _applicants;
  }

  static new(user: User): User {
    return this.empty().update(user);
  }

  static empty(): User {
    return new User(0, false, false, '', '', null, '', null, '', [], [], [], [], []);
  }

  update(user: User): User {
    if (user) {
      this.id = user.id;
      this.inactive = user.inactive;
      this.ban = user.ban;
      this.email = user.email;
      this.name =user.name;
      this.discord = user.discord;
      this.linkedin = user.linkedin;
      this.contacts = user.contacts;
      this.profilePictureUrl =user.profilePictureUrl;
      this.roles = user.roles;
      this.students = user.students;
      this.mentors =user.mentors;
      this.applicants = user.applicants;  
      return this;
    }
    return User.empty();
  }

  get isAuthenticated(): boolean {
    return this.roles.length != 0;
  }

  hasAnyRole(...roles: string[]): boolean {
    return roles.some(roleName => this.roles?.some(userRole => userRole === roleName));
  }

  hasGuestRole(): boolean {
    return this.hasAnyRole(Roles.GUEST);
  }

  hasStudentRole(): boolean {
    return this.hasAnyRole(Roles.STUDENT);
  }

  hasGraduateRole(): boolean {
    return this.hasAnyRole(Roles.GRADUATE);
  }

  hasMentorRole(): boolean {
    return this.hasAnyRole(Roles.MENTOR);
  }

  hasComradeRole(): boolean {
    return this.hasAnyRole(Roles.COMRADE);
  }

  hasStaffRole() : boolean {
    return this.hasAnyRole(Roles.STAFF);
  }

  hasAdminRole(): boolean {
    return this.hasAnyRole(Roles.ADMIN);
  }

  hasAdvancedRole(): boolean {
    return this.roles?.some(role => Roles.ADVANCED.some(advancedRole => advancedRole === role));
  }


  getApplicant(slug: string): StateApplicant | undefined {
    return this.applicants?.find(applicant => applicant.slug === slug);
  }

  getStudent(slug: string): StateStudent | undefined {
    return this.students?.find(student => student.slug === slug);
  }

  getGraduate(slug: string): StateGraduate | undefined {
    return this.graduates?.find(graduate => graduate.slug === slug);
  }

  getMentor(slug: string): StateMentor | undefined {
    return this.mentors?.find(mentor => mentor.slug === slug);
  }


  isStudent(slug:string): boolean {
    return this.hasStudentRole() && (this.getStudent(slug) != undefined);
  }

  isGraduate(slug:string): boolean {
    return this.hasGraduateRole() && (this.getGraduate(slug) != undefined);
  }

  isMentor(slug: string): boolean {
    return this.hasMentorRole() && (this.getMentor(slug) != undefined);
  }

  isApplicant(slug: string): boolean {
    return this.getApplicant(slug) != undefined;
  }

  hasApplicantId(id: number): boolean {
    return this.applicants?.some(applicant => applicant.applicantId == id);
  }


  getStudentSlugs(): string[] {
    return this.students.map(student => student.slug);
  }
 
  getGraduateSlugs(): string[] {
    return this.graduates.map(graduate => graduate.slug);
  }
 
  getMentorSlugs(): string[] {
    return this.mentors.map(mentor => mentor.slug);
  }
 
  getApplicantSlugs(): string[] {
    return this.applicants.map(applicant => applicant.slug);
  }
 

  addAplicant(applicant: StateApplicant): void {
    this.applicants.push(applicant);
  }

  addStudent(student: StateStudent): void {
    this.students.push(student);
    this.checkRoles();
  }

  addGraduate(graduate: StateGraduate): void {
    this.graduates.push(graduate);
    this.checkRoles();
  }

  addMentor(mentor: StateMentor): void {
    this.mentors.push(mentor);
    this.checkRoles()
  }


  removeApplicant(slug: string): void {
    this.applicants = this.applicants.filter(applicant => applicant.slug !== slug);
  }

  removeApplicantById(id: number): void {
    this.applicants = this.applicants.filter(applicant => applicant.applicantId != id);
  }

  removeStudentById(id: number): void {
    this.students = this.students.filter(student => student.id != id);
  }

removeMentorById(mentorId: number): void {
    this.mentors = this.mentors.filter(mentor => mentor.mentorId != mentorId);
    this.checkRoles()
  }


  checkRoles(): void {
    if (this.mentors.length == 0)
      this.removeRole(ROLE_MENTOR);
    if (this.mentors.length > 0 && !this.hasMentorRole())
      this.addRole(ROLE_MENTOR);
    if (this.students.length == 0)
      this.removeRole(ROLE_STUDENT);
    if (this.students.length > 0 && !this.hasStudentRole())
      this.addRole(ROLE_STUDENT);
    if (this.roles.length == 0)
      this.addRole(ROLE_GUEST);
    if (this.roles.length > 1)
      this.removeRole(ROLE_GUEST);
  }

  addRole(newRole: string): void {
    if (!this.roles.some(role => role === newRole)) {
      this.roles.push(newRole);
      this.checkRoles()
    }
  }

  removeRole(oldRole: string): void {
    if (this.roles.some(role => role === oldRole)) {
      this.roles = this.roles.filter(role => role !== oldRole);
      this.checkRoles()
    }
  }


  maybeNewStudent(applicant: Applicant): boolean {
    if (applicant.applied && this.hasApplicantId(applicant.id)) {
      this.removeApplicantById(applicant.id);
      this.addStudent(applicant.student!)
      return true;
    }
    return false; 
  }

  maybeNotApplicant(applicant: Applicant): boolean {
    if (applicant.rejected && this.hasApplicantId(applicant.id)) {
      this.removeApplicantById(applicant.id);
      return true;
    }
    return false; 
  }

  maybeNotStudent(student: StateStudent): boolean {
    if (student.activeChapterNumber == 0 && student.inactive) {
      this.removeStudentById(student.id);
      this.addGraduate(student);
      return true;
    }
    return false;
  }

  setActiveChapterNumber(slug: string, number:number): void {
      this.getStudent(slug)!.activeChapterNumber = number;
  }

  swapNameSurname(): void {
    this.name = this.name.split(' ').reverse().join(' ')
  }

  getCourseActiveChapterNumber(slug: string): number {
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
        json.roles,
        json.students,
        json.graduates,
        json.mentors,
        json.applicants
    );
  }

}
