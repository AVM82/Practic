export enum ApiUrls {
  Me = '/api/persons/me',
  Persons = '/api/persons',
  Application = '/api/persons/application/',

  Certification = '/api/certification/',
  
  Feedbacks = '/api/feedbacks',

  Quizzes = '/api/quizzes/',

  Courses = '/api/courses',
  Course = '/api/courses/',
  NewCourse = '/api/courses/NewCourse',
  NewCourseFromProperties = '/api/courses/NewCourseFromProperties',
  Chapters = '/api/courses/chapters/',

  Students = '/api/students',
  StudentChapters = '/api/students/chapters/',
  StudentSkills = '/api/students/skills/',
  StudentChapterStates = '/api/students/chapters/states/',
  StudentAdditionalMaterials = '/api/students/additionalMaterials/',
  Practices = '/api/students/practices/',
  PracticeStates = '/api/students/practices/states/',
  Reports = '/api/students/reports/',
  ReportStates = '/api/students/reports/states',
  ReportLikeList = '/api/students/reports/likes/',
  MyPractices = '/api/students/practices/my',
  TopicsReports = '/api/topicsreports/',
  StudentChapterTopicsReports = '/api/students/topicsreports/',

  Mentors = '/api/mentors/',
  Applicants = '/api/mentors/applicants',
  CourseStudents = '/api/mentors/students',
  MentorPractices = '/api/mentors/practices',

  EmailPassAuth = 'api/auth',
  EmailPassRegister = 'api/register',

  CalendarEventEmailNotification = '/api/events/sendEvent/',
  
  SendSecretCode = '/api/password-reset/send-code',
  MatchCode = '/api/password-reset/match-code',
  ResetPassword = '/api/password-reset',
  VerificateByEmail = 'api/verification',
  MatchTokenForVerificateByEmail = 'api/verification/match-token',
  Profile ='api/persons/profile'

}

export const sendCalendarEventEmailNotificationUrl = (slug: string): string =>
    ApiUrls.CalendarEventEmailNotification + slug ;
export const getChaptersUrl = (slug: string): string =>
    ApiUrls.Course + slug + `/allchapters`;

export const getMaterialsUrl = (slug: string): string =>
    ApiUrls.Course + slug + `/additional`;

export const getMaterialsExistUrl = (slug: string): string =>
    ApiUrls.Course + slug + `/additionalExist`;

export const getChapterUrl = (slug: string, chapterN: number): string =>
    ApiUrls.Course + slug + `/chapters/` + chapterN;

export const getCourseUrl = (slug: string): string =>
    ApiUrls.Course + slug;

export const getReportsUrl = (slug: string): string =>
    `/api/students/reports/course/${slug}`;

export const postReportsUrl = (studentChapterId: number): string =>
    `/api/students/reports/${studentChapterId}`;

export const getLevelsUrl = (slug: string): string =>
    `/api/courses/${slug}/levels`;

export const timeslotsUrl = (slug: string): string =>
    `/api/students/reports/course/${slug}/timeslots`;

export const getStudentsAllAdditionalMaterialsUrl = (studentId: number): string =>
    ApiUrls.StudentAdditionalMaterials + studentId;

export const getStudentAdditionalMaterialUrl = (studentId: number, addId: number): string =>
    getStudentsAllAdditionalMaterialsUrl(studentId) + `/` + addId;

export const deleteReportsUrl = (reportId: number): string =>
    `/api/students/reports/course/${reportId}`;

export const getApplicationUrl = (slug: string): string =>
    ApiUrls.Application + slug;

export const getApplicationCheckUrl = (id: number): string =>
    ApiUrls.Application + id;

export const getStudentChaptersUrl = (studentId: number): string =>
    ApiUrls.StudentChapters + studentId;

export const getStudentChapterUrl = (studentId: number, chapterN: number): string =>
    ApiUrls.StudentChapters + studentId + `/` + chapterN;

export const addRoleUrl = (id: number, role: string): string =>
    ApiUrls.Persons + `/` + id + `/add/` + role;

export const removeRoleUrl = (id: number, role: string): string =>
    ApiUrls.Persons + `/` + id + `/remove/` + role;

export const addMentorUrl = (slug: string, id: number): string =>
    ApiUrls.Mentors + `add/`+ slug + `/` + id;

export const removeMentorUrl = (id: number): string =>
    ApiUrls.Mentors + `remove/` + id;

export const getSetChapterStateUrl = (chapterId: number, newState: string): string =>
    ApiUrls.StudentChapterStates + chapterId + `/` + newState;
