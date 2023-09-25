export enum ApiUrls {
  Courses = '/api/courses',
  Course = '/api/courses/',
  NewCourseFromProperties = Courses + '/NewCourseFromProperties',
  Students = '/api/students',
  Practices = Students + '/practices/',
  PracticeStates = Students + '/practices/states',
  Reports = Students + '/reports/course/',
  ReportStates = Students + '/reports/states',
  Chapters = '/api/chapters',
  Me = '/api/persons/me',
  Applicants = '/api/persons/applicants',
  OpenChapters = Students + '/chapters',
  PracticeState = Students + '/practices',
  PracticeApprove = '/api/mentor/practices',
  ReportLikeList = Students + '/reports/likes/'
}

export const getChaptersUrl = (slug: string): string =>
    ApiUrls.Course + slug + `/allchapters`;

export const getMaterialsUrl = (slug: string): string =>
    ApiUrls.Course + slug + `/additional`;

export const getChapterUrl = (slug: string, chapterN: number): string =>
    ApiUrls.Course + slug + `/chapters/` + chapterN;

export const getCourseUrl = (slug: string): string =>
    ApiUrls.Course + slug;

export const getReportsUrl = (slug: string): string =>
    `/api/students/reports/course/${slug}`;

export const getLevelsUrl = (slug: string): string =>
    `/api/courses/${slug}/levels`;

export const timeslotsUrl = (slug: string): string =>
    `/api/students/reports/course/${slug}/timeslots`;
