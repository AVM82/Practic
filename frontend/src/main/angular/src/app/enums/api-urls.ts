export enum ApiUrls {
  Courses = '/api/courses',
  Course = '/api/courses/',
  Practices = '/api/students/practices/',
  PracticeStates = '/api/students/practices/states',
  Reports = '/api/students/reports/course/',
  ReportStates = '/api/students/reports/states',
  Chapters = '/api/chapters',
  Me = '/api/persons/me'
}

export const getChapterByIdUrl = (courseId: number): string =>
    `/api/courses/${courseId}/chapters`;

export const getChaptersUrl = (courseId: number): string =>
    `/api/courses/${courseId}/chapters`;

export const getChapterUrl = (chapterId: number): string =>
    `/api/chapters/${chapterId}`;