import {SubSubChapter} from "./subsubchapter";
import {ReferenceTitle} from "./referenceTitle";

export interface SubChapter {
  id: number;
  number: number;
  name: string;
  refs?: ReferenceTitle[];
  subSubChapters : SubSubChapter[];
}
