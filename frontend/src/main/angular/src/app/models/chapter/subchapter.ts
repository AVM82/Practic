export class SubChapter {
  id: number;
  number: number;
  name: string;
  refs: string;
  subchapters : SubSubChapter[];
  isVisible: boolean;

  constructor(id: number, number: number, name: string, refs: string, subchapters : SubSubChapter[]) {
    this.id = id;
    this.number = number;
    this.name = name;
    this.refs = refs;
    this.subchapters = subchapters;
    this.isVisible = false;
  }
}
