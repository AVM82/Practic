import {ChapterPart} from "./chapterpart";

export interface Chapter {
  id: number;
  number: number;
  name: string;
  parts: ChapterPart[];
}
