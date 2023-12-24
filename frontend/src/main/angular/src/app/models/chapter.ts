import {ChapterPart} from "./chapterpart";
import {STATE_NOT_STARTED} from 'src/app/enums/app-constans';
import {Report, TopicReport} from "./report";
import { ReportService } from "../services/report.service";

export class Chapter {
  id: number;
  number: number;
  hidden: boolean;
  shortName: string;
  name?: string;
  parts: ChapterPart[];
  skills?: string[];
  state: string;
  reports: Report[];
  myReports:  Report[];
  subs?: number[];
  topicReports?: TopicReport [];
  isQuizPassed: boolean = false;
  isQuizPassed: boolean;
  quizId: number;
  
  constructor(
    id: number,
    number: number,
    hidden: boolean,
    shortName: string,
    parts: ChapterPart[],
    state: string,
    myReports:  Report[],
    reports: Report[],
    topicReports: TopicReport []
  ) {
    this.id = id;
    this.number = number;
    this.hidden = hidden;
    this.shortName = shortName;
    this.parts = parts; 
    this.state = state || STATE_NOT_STARTED;
    this.reports = reports;
    this.myReports = myReports;
    this.topicReports = topicReports;
  }

  public static complete(chapter: Chapter, ext: CompleteChapter) {
    chapter.id = ext.id;
    chapter.name = ext.name;
    chapter.parts = ext.parts;
    chapter.skills = ext.skills;
    chapter.reports = ext.reports;
    ReportService.refreshMyReports(chapter);
    chapter.subs = ext.subs;
  }
}

export interface CompleteChapter {
  id: number;
  name: string;
  parts: ChapterPart[];
  skills: string[];
  myReports: Report[];
  reports: Report[];
  subs: number[];
}

export interface NewStateChapter {
  state: string;
  activeChapterNumber: number;
  studentChapterId: number;
}
