import { Practice } from "./practice";
import {ChapterPart} from "./chapterpart";

interface BaseChapter {
  id: number;
  number: number;
  reportCount: number;
  state: string;
  practices: Practice[];
}

export interface Chapter extends BaseChapter {
  name: string;
  parts: ChapterPart[];
}

export interface ShortChapter extends BaseChapter {
  shortName: string;
  hidden: boolean;
}
