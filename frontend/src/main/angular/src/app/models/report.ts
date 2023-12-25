import { Chapter } from "./chapter";

export class Report {
  id: number;
  personId: number;
  personName: string;
  profilePictureUrl: string;
  date: Date;
  topic: TopicReport;
  studentChapterId: number;
  chapterNumber: number;
  state: string;
  likedPersonIds: number[];

  constructor(
    id: number,
    personId: number,
    personName: string,
    profilePictureUrl: string,
    date: Date,
    topic: TopicReport,
    studentChapterId: number,
    chapterNumber: number,
    state: string,
    likedStudentsIdList: number[]
  ) {
    this.id = id
    this.personId = personId
    this.personName = personName
    this.profilePictureUrl = profilePictureUrl
    this.date = date
    this.topic = topic
    this.studentChapterId = studentChapterId
    this.chapterNumber = chapterNumber
    this.state = state
    this.likedPersonIds = likedStudentsIdList
  }

}

export class FrontReport {
  reportId: number = 0;
  studentChapterId: number = 0;
  personId: number = 0;
  date?: Date;
  chapterNumber: number = 0;
  topicReportId: number = 0;

  constructor(form: FormReport, isStudent: boolean, myId: number) {
    this.reportId = form.id;
    if (isStudent)
      this.studentChapterId = form.chapter.id;
    this.personId = myId;
    this.date = form.date;
    this.chapterNumber = form.chapter.number;
    this.topicReportId = form.topic.id;
  }

}

export interface FormReport {
  id: number;
  chapter: Chapter;
  date: Date;
  topic: TopicReport;
}

export interface TopicReport {
  id: number;
  topic: string;
}
