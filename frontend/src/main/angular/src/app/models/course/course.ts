import {Chapter} from "./chapter";

export class Course {
  id: number;
  inactive: boolean | undefined;
  authors: string | undefined;
  courseType: string | undefined;
  name: string;
  chapters: Chapter[];

  constructor(id: number, name: string, chapters: Chapter[]) {
    this.id = id;
    this.name = name;
    this.chapters = chapters;
  }
}
