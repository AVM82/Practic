export interface Mentor {
    id: number;
    name: string;
    slug: string;
    linkedInUrl: string;
}

export interface StateMentor {
    mentorId: number;
    slug: string;
    linkedInUrl: string;
}

export interface MentorComplex {
    mentorDto: Mentor;
    stateMentor: StateMentor;
}

