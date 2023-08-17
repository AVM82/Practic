export enum ApiUrls {
  Courses = '/api/courses',
  Course = '/api/course/',
}

export const getChaptersUrl = (courseId: number): string =>
    `/api/courses/${courseId}/chapters`;
