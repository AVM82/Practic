export enum ApiUrls {
  Courses = '/api/courses',
  Course = '/api/courses/',
  Practices = '/api/students/practices/',
  PracticeStates = '/api/students/practices/states',
  Reports = '/api/students/reports/course/',
  ReportStates = '/api/students/reports/states',
  Chapters = '/api/chapters',
  Me = '/api/persons/me',
  Applicants = '/api/persons/applicants',
  OpenChapters = '/api/students/chapters',
  PracticeState = '/api/students/practices',
  PracticeApprove = '/api/mentor/practices',
  Feedbacks = '/api/feedbacks/',
  ReportLikeList = '/api/students/reports/likes/',
  MyPractices = '/api/students/practices/my'
  }


export const getChaptersUrl = (slug: string): string =>
    ApiUrls.Course + slug + `/allchapters`;

export const getMaterialsUrl = (slug: string): string =>
    ApiUrls.Course + slug + `/additional`;

export const getChapterUrl = (slug: string, chapterN: number): string =>
    ApiUrls.Course + slug + `/chapters/` + chapterN;

export const getCourseUrl = (slug: string): string =>
    ApiUrls.Course + slug;

export const postCourse = window.location.origin + ApiUrls.Courses;

export const getReportsUrl = (slug: string): string =>
    `/api/students/reports/course/${slug}`;
export const postReportsUrl = (slug: string): string =>
    `/api/students/reports/course/${slug}`;

export const getLevelsUrl = (slug: string): string =>
    `/api/courses/${slug}/levels`;

export const timeslotsUrl = (slug: string): string =>
    `/api/students/reports/course/${slug}/timeslots`;
export const deleteReportsUrl = (reportId: number): string =>
    `/api/students/reports/course/${reportId}`;
