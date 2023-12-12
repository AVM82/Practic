import { Practice } from "./practice";
import {SubChapter} from "./subchapter";

export class ChapterPart {
  common?: CommonPart;
  practice: Practice;

  constructor(
    practice: Practice,
    common?: CommonPart
  ){
    this.common = common;
    this.practice = practice;
  }

}

export interface CommonPart {
  id: number;
  number: number;
  subChapters: SubChapter[];
  praxisPurpose: string;
  praxis: SubChapter[];
  additionals: SubChapter[];
}
