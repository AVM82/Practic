export enum ApiUrls {
  Courses = '/api/courses',
  Course = '/api/courses/',
  Practices = '/api/students/practices/',
  PracticeStates = '/api/students/practices/states'
}

export const getChapterByIdUrl = (courseId: number): string =>
    `/api/courses/${courseId}/chapters`;
