export class Course {
  id: number;
  name: string;
  chapters: Chapter[];

  constructor(id: number, name: string, chapters: Chapter[]) {
    this.id = id;
    this.name = name;
    this.chapters = chapters;
  }
}

export class Chapter {
  id: number;
  title: string;
  completed: boolean;
  visible: boolean;



  constructor(id: number, title: string, completed: boolean, visible: boolean) {
    this.id = id;
    this.title = title;
    this.completed = completed;
    this.visible = visible;
  }
}

const sampleChapters: Chapter[] = [
  { id: 1, title: 'Розділ 1', completed: false, visible: true },
  { id: 2, title: 'Розділ 2', completed: false, visible: false },
  { id: 3, title: 'Розділ 3', completed: false, visible: false },
];

export const sampleCourse: Course = {
  id: 1,
  name: 'Java',
  chapters: sampleChapters,
};
