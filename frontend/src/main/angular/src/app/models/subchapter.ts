import {SubSubChapter} from "./subsubchapter";
import {ReferenceTitle} from "./referenceTitle";

export class SubChapter {
  id: number;
  number: number;
  name: string;
  refs?: ReferenceTitle[];
  subSubChapters? : SubSubChapter[];
  skills: string[];
  checked: boolean = false;;

  constructor(
    id: number,
    number: number,
    name: string,
    refs?: ReferenceTitle[],
    subSubChapters? : SubSubChapter[],
    skills?: string[]
  ){
    this.id = id;
    this.number = number;
    this.name = name;
    this.refs = refs;
    this.subSubChapters = subSubChapters;
    this.skills = skills || [];
  }
}
