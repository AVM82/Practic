export interface Applicant {
    id: number;
    name: string;
    slug: string;
    isApplied: boolean;
}

export interface StateApplicant {
    applicantId: number;
    slug: string;
}
