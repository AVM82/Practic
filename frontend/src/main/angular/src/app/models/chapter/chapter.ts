import { Practice } from "../practice/practice";
import {ChapterPart} from "./chapterpart";

export interface Chapter {
  id: number;
  number: number;
  name: string;
  parts: ChapterPart[];
  state: string;
  practices: Practice[];
}
