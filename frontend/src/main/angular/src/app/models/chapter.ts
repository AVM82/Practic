import { Practice } from "./practice";
import {ChapterPart} from "./chapterpart";

export interface Chapter {
  id: number;
  number: number;
  name: string;
  parts: ChapterPart[];
  state: string;
  practices: Practice[];
}

export interface ShortChapter {
  id: number;
  number: number;
  shortName: string;
  visible: boolean;
  partsId: number[];
}
