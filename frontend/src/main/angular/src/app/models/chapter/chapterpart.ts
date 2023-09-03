import {SubChapter} from "./subchapter";

export interface ChapterPart {
  id: number;
  number: number;
  subChapters: SubChapter[];
  praxisPurpose: string;
  praxis: SubChapter[];
  additionals: SubChapter[];
}
