import { Practice } from "../practice/practice";

export interface UserCourses {
    slug: string;
    inactive: boolean;
    ban: boolean;
    studentId: number;
    activeChapterNumber: number;
    reportOnce: boolean;
}