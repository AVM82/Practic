import {SubChapter} from "./subchapter";

export interface Chapter {
  id: number;
  number: number;
  name: string;
  subChapters: SubChapter[];
}
