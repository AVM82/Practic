import { Practice } from "./practice";
import {SubChapter} from "./subchapter";

export interface ChapterPart {
  common?: CommonPart;
  practice: Practice,
}

export interface CommonPart {
  id: number;
  number: number;
  subChapters: SubChapter[];
  praxisPurpose: string;
  praxis: SubChapter[];
  additionals: SubChapter[];
  state: string;
}
