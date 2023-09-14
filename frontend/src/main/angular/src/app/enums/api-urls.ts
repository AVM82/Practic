export enum ApiUrls {
  Courses = '/api/courses',
  Course = '/api/courses/',
  Practices = '/api/students/practices/',
  PracticeStates = '/api/students/practices/states',
  ReportStates = '/api/students/reports/states',
  TimeSlots = '/api/students/timeslots',
  Me = '/api/persons/me'
}

export const getChaptersUrl = (slug: string): string =>
    `/api/courses/${slug}/allchapters`;

   export const getMaterialsUrl = (slug: string): string =>
    `/api/courses/${slug}/additional`;

export const getChapterUrl = (slug: string, chapterN: number): string =>
    `/api/courses/${slug}/chapters/${chapterN}`;

export const getReportsUrl = (slug: string): string =>
    `/api/students/reports/course/${slug}`;
export const postReportsUrl = (slug: string): string =>
    `/api/students/reports/course/${slug}`;