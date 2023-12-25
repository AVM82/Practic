import { StateStudent } from "./student";

export class Applicant {
    id: number;
    name: string;
    createdAt: string;
    applied: boolean;
    rejected: boolean;
    student: StateStudent | undefined;

    constructor(a: Applicant){
        this.id = a.id;
        this.name = a.name;
        this.createdAt = a.createdAt;
        this.applied = a.applied;
        this.rejected = a.rejected;
        this.student = a.student;
    }

    update(newApplicant: Applicant): this {
        this.id = newApplicant.id;
        this.name = newApplicant.name;
        this.createdAt = newApplicant.createdAt;
        this.applied = newApplicant.applied;
        this.rejected = newApplicant.rejected;
        this.student = newApplicant.student;
        return this;
    }

}

export interface StateApplicant {
    applicantId: number;
    slug: string;
}

export class CourseApplicants {
    courseName: string;
    applicants: Applicant[];

    constructor(
        _courseName: string,
        _applicants: Applicant[]
    ) {
        this.courseName = _courseName;
        this.applicants = _applicants;
    }
}
