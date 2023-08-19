export enum ApiUrls {
  Courses = '/api/courses',
  Course = '/api/courses/',
  Chapters = '/api/chapters'
}

export const getChaptersUrl = (courseId: number): string =>
    `/api/courses/${courseId}/chapters`;

export const getChapterUrl = (courseId: number, chapterId: number): string =>
    `/api/courses/${courseId}/chapters/${chapterId}`;
