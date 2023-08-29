import {SubSubChapter} from "./subsubchapter";


export interface SubChapter {
  id: number;
  number: number;
  name: string;
  refs?: string ;
  subSubChapters : SubSubChapter[];
}
