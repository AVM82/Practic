import {SubChapter} from "./subchapter";

export class Chapter {
  id: number;
  number: number;
  name: string;
  subchapters: SubChapter[];

  constructor(id: number, number: number, name: string, subchapters: SubChapter[]) {
    this.id = id;
    this.number = number;
    this.name = name;
    this.isVisible = false;
    this.subchapters = subchapters;
  }
}
