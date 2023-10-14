import {ChapterPart} from "./chapterpart";

export interface Chapter {
  id: number;
  number: number;
  shortName: string;
  name: string;
  parts: ChapterPart[];
  visible: boolean;
}
