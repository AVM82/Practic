export enum ApiUrls {
  Courses = '/api/courses',
  Course = '/api/courses/',
  NewCourse = '/api/courses/NewCourse',
  NewCourseFromProperties = '/api/courses/NewCourseFromProperties',
  Students = '/api/students',
  Practices = '/api/students/practices/',
  PracticeStates = '/api/students/practices/states',
  Reports = '/api/students/reports/course/',
  ReportStates = '/api/students/reports/states',
  AdditionalMaterials = '/api/students/additionalMaterials/',
  Chapters = '/api/chapters',
  Me = '/api/persons/me',
  Applicants = '/api/persons/applicants',
  OpenChapters = '/api/students/chapters/',
  PracticeState = '/api/students/practices',
  PracticeApprove = '/api/mentor/practices',
  Feedbacks = '/api/feedbacks/',
  ReportLikeList = '/api/students/reports/likes/',
  MyPractices = '/api/students/practices/my',
  TopicsReports = '/api/topicsreports'
}


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

export const getLevelsUrl = (slug: string): string =>
    `/api/courses/${slug}/levels`;

export const timeslotsUrl = (slug: string): string =>
    `/api/students/reports/course/${slug}/timeslots`;

export const getDescriptionUrl = (slug: string): string =>
    ApiUrls.Course + slug + `/description`;

export const getStudentsAllAdditionalMaterialsUrl = (slug: string): string =>
    ApiUrls.AdditionalMaterials + slug;

export const getStudentAdditionalMaterialUrl = (slug: string, id: number): string =>
    ApiUrls.AdditionalMaterials + slug + `/` + id;

export const getActiveChapterNumber = (slug: string): string =>
    ApiUrls.Course+ slug +`/activeChapterNumber`;

export const deleteReportsUrl = (reportId: number): string =>
    `/api/students/reports/course/${reportId}`;

export const getApplicationUrl = (slug: string): string =>
    `/api/persons/application/` + slug;

export const getApplicationCheckUrl = (id: number): string =>
    `/api/persons/application/` + id;

export const getOpenedChaptersUrl = (slug: string): string =>
    ApiUrls.OpenChapters + slug;

export const getStudentChapterUrl = (slug: string, chapterN: number): string =>
    ApiUrls.OpenChapters + slug + `/` + chapterN;
