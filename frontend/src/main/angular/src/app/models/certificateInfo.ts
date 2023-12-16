
export class CertificateInfo {
    constructor(public studentName: string,
                public courseName: string,
                public skills: string,
                public start: Date,
                public finish: Date,
                public daysSpent: number) {
    }
}

export interface Skill {
    name: string;
    enableSkill: boolean;
}