export enum ApiUrls {
  Courses = '/api/courses',
  Course = '/api/courses/',
  Practices = '/api/students/practices/',
  PracticeStates = '/api/students/practices/states',
  Reports = '/api/students/reports/course/',
  ReportStates = '/api/students/reports/states',
  Chapters = '/api/chapters',
  Me = '/api/persons/me',
  Applicants = '/api/persons/?inactive=true'
}

export const getChaptersUrl = (slug: string): string =>
    `/api/courses/${slug}/allchapters`;

export const getChapterUrl = (slug: string, chapterN: number): string =>
    `/api/courses/${slug}/chapters/${chapterN}`;