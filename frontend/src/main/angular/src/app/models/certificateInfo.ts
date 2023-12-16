
export class CertificateInfo {
    selectedSkills: Skill[] = [];
    constructor(public studentName: string,
        public courseName: string,
        public skills: string[],
        public start: Date,
        public finish: Date,
        public daysSpent: number) {
        skills.forEach(s => this.selectedSkills.push(new Skill(s)));
    }
}

export class Skill {
    enabledSkill: boolean = true;
    constructor(public name: string) { }
}