import { Practice } from "./practice";
import { Report } from "src/app/models/report";

export interface StateGraduate {
    id: number;
    inactive: boolean;
    ban: boolean;
    slug: string;
}

export interface StateStudent {
    id: number;
    inactive: boolean;
    ban: boolean;
    slug: string;
    activeChapterNumber: number;
}

export class Student {
    id: number;
    inactive: boolean;
    ban: boolean;
    name: string;
    activeChapterNumber: number;
    chapterState: string;
    practices: Practice[];
    reports: Report[];

    constructor(student: Student) {
        this.id = student.id;
        this.inactive = student.inactive;
        this.ban = student.ban;
        this.name = student.name;
        this.activeChapterNumber = student.activeChapterNumber;
        this.chapterState = student.chapterState;
        this.practices = student.practices;
        this.reports = student.reports;
    }

}

export class CourseStudents {
    courseName: string;
    chapterNumbers: number[];
    students: Student[];

    constructor(
        courseName: string,
        chapterNumbers: number[],
        students: Student[]
    ) {
        this.courseName = courseName;
        this.chapterNumbers = chapterNumbers;
        this.students = students;
    }

}
