import {ChapterPart} from "./chapterpart";
import {STATE_NOT_STARTED} from 'src/app/enums/app-constans';

class BaseChapter {
  id: number;
  number: number;
  partsCount: number;
  reportCount: number;
  myReports: number; //list
  state: string;
  parts: ChapterPart[];

  constructor(
    _id: number,
    _number: number,
    _partsCount: number,
    _reportCount: number,
    _myReports: number,
    _state: string,
    _parts: ChapterPart[]
  ) {
    this.id = _id;
    this.number = _number;
    this.partsCount = _partsCount;
    this.reportCount = _reportCount | 0;
    this.myReports = _myReports | 0;
    this.state = _state || STATE_NOT_STARTED;
    this.parts = _parts || []; 
  }

}

export class Chapter extends BaseChapter {
  name: string;

  constructor(
    _id: number,
    _number: number,
    _partsCount: number,
    _reportCount: number,
    _myReports: number,
    _state: string,
    _parts: ChapterPart[],
    _name: string,
  ) {
    super(_id, _number, _partsCount, _reportCount, _myReports, _state, _parts);
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
    _partsCount: number,
    _reportCount: number,
    _myReports: number,
    _state: string,
    _parts: ChapterPart[],
    _shortName: string,
    _hidden: boolean
  ) {
    super(_id, _number, _partsCount, _reportCount, _myReports, _state, _parts);
    this.shortName = _shortName;
    this.hidden = _hidden;
  }

}

export interface NewStateChapter {
  state: string;
  activeChapterNumber: number;
  studentChapterId: number;
}
