import {SubSubChapter} from "./subsubchapter";
import {ReferenceTitle} from "../reference/referenceTitle";

export interface SubChapter {
  id: number;
  number: number;
  name: string;
  refs?: ReferenceTitle[];
  subSubChapters : SubSubChapter[];
}
