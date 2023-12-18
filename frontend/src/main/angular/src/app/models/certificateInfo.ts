export class CertificateInfo {
    public selectedSkills: Skill[] = [];
    public studentName: string = "";


    constructor(
        studentName: string,
        public courseName: string,
        public skills: string[],
        public start: Date,
        public finish: Date,
        public daysSpent: number) {
        this.studentName = studentName;
        this.skills.forEach(skill => this.selectedSkills.push(new Skill(skill)));
    }
}

export class Skill {
    enabledSkill: boolean = true;

    constructor(public name: string) {
    }
}