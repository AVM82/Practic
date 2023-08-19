import {SubSubChapter} from "./subsubchapter";


export class SubChapter {
  id: number;
  number: number;
  name: string;
  refs: string | undefined;
  subsubchapters : SubSubChapter[];

  constructor(id: number, number: number, name: string, refs: string, subsubchapters : SubSubChapter[]) {
    this.id = id;
    this.number = number;
    this.name = name;
    this.refs = refs;
    this.subsubchapters = subsubchapters;
  }
}
