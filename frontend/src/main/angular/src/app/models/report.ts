export interface Report {
  id: number;
  studentId: number;
  date: any;
  time: any;
  timeslotId:number;
  state: string;
  title: string;
  likedStudentsIdList: number[];
}

export interface StudentReport {
  id:number;
  personId:number;
  personName: string;
  profilePictureUrl: string;
  chapterName: string;
  chapterId:number;
  chapterNumber: number;
  date: any;
  time: any;
  timeslotId:number;
  state: string;
  title: string;
  likedPersonsIdList:any;
}

export interface TopicReport{
  chapterId:number
  topicReport:string
}
