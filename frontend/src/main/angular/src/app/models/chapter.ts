import {ChapterPart} from "./chapterpart";
import { Practice } from "./practice";

interface BaseChapter {
  id: number;
  number: number;
  reportCount: number;
  myReports: number;
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

export interface NewStateChapter {
  state: string;
  activeChapterNumber: number;
}
