import {ChapterPart} from "./chapterpart";
import {ReportState, STATE_NOT_STARTED} from 'src/app/enums/app-constans';
import {StudentReport, TopicReport} from "./report";

export class Chapter {
  id: number;
  number: number;
  hidden: boolean;
  shortName: string;
  name?: string;
  parts: ChapterPart[];
  skills?: string[];
  state: string;
  reportCount: number;
  myReports: number;
  reports?: StudentReport[];
  subs?: number[];
  topicReports?: TopicReport [];

  constructor(
    id: number,
    number: number,
    hidden: boolean,
    shortName: string,
    parts: ChapterPart[],
    state: string,
    reportCount: number,
    myReports: number
  ) {
    this.id = id;
    this.number = number;
    this.hidden = hidden;
    this.shortName = shortName;
    this.parts = parts; 
    this.reportCount = reportCount;
    this.state = state || STATE_NOT_STARTED;
    this.myReports = myReports;
  }

  public static complete(chapter: Chapter, ext: CompleteChapter) {
    chapter.id = ext.id;
    chapter.name = ext.name;
    chapter.parts = ext.parts;
    chapter.skills = ext.skills;
    chapter.reportCount = ext.reportCount;
    chapter.reports = ext.reports;
    if (ext.reports)
      chapter.myReports = ext.reports.filter(report => ReportState.isActual(report.state)).length;
    chapter.subs = ext.subs;
    chapter.topicReports = ext.topicReports;
  }
}

export interface CompleteChapter {
  id: number;
  name: string;
  parts: ChapterPart[];
  skills: string[];
  reportCount: number;
  reports: StudentReport[];
  subs: number[];
  topicReports: TopicReport [];
}

export interface NewStateChapter {
  state: string;
  activeChapterNumber: number;
  studentChapterId: number;
}
