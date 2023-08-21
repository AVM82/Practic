export enum ApiUrls {
  Courses = '/api/courses',
  Course = '/api/courses/',
  Practices = '/api/students/practices/',
  PracticeStates = '/api/students/practices/states',
  Chapters = '/api/chapters'
}

export const getChapterByIdUrl = (courseId: number): string =>
    `/api/courses/${courseId}/chapters`;

export const getChaptersUrl = (courseId: number): string =>
    `/api/courses/${courseId}/chapters`;

export const getChapterUrl = (courseId: number, chapterId: number): string =>
    `/api/courses/${courseId}/chapters/${chapterId}`;
