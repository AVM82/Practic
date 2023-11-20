import {ChapterPart} from "./chapterpart";
import { Practice } from "./practice";
import {STATE_NOT_STARTED} from 'src/app/enums/app-constans';

class BaseChapter {
  id: number;
  number: number;
  reportCount: number;
  myReports: number;
  state: string;
  practices!: Practice[];

  constructor(
    _id: number,
    _number: number,
    _reportCount: number,
    _myReports: number,
    _state: string,
    _practices: Practice[]
  ) {
    this.id = _id;
    this.number = _number;
    this.reportCount = _reportCount | 0;
    this.myReports = _myReports | 0;
    this.state = _state || STATE_NOT_STARTED;
    this.practices = _practices || []; 
  }

}

export class Chapter extends BaseChapter {
  name: string;
  parts: ChapterPart[];

  constructor(
    _id: number,
    _number: number,
    _reportCount: number,
    _myReports: number,
    _state: string,
    _practices: Practice[],
    _name: string,
    _parts: ChapterPart[]
  ) {
    super(_id, _number, _reportCount, _myReports, _state, _practices);
    this.name = _name;
    this.parts = _parts;
  }
}

export class ShortChapter extends BaseChapter {
  shortName: string;
  hidden: boolean;

  constructor(
    _id: number,
    _number: number,
    _reportCount: number,
    _myReports: number,
    _state: string,
    _practices: Practice[],
    _shortName: string,
    _hidden: boolean
  ) {
    super(_id, _number, _reportCount, _myReports, _state, _practices);
    this.shortName = _shortName;
    this.hidden = _hidden;
  }

}

export interface NewStateChapter {
  state: string;
  activeChapterNumber: number;
  studentChapterId: number;
}
