import { StateStudent } from "./student";

export interface Applicant {
    id: number;
    name: string;
    slug: string;
    createdAt: string;
    isApplied: boolean;
    isRejected: boolean;
    student: StateStudent;
}

export interface StateApplicant {
    applicantId: number;
    slug: string;
}

export interface CourseApplicants {
    slug: string;
    applicants: Applicant[];
}