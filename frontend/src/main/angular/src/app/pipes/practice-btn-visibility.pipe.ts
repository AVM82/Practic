import { Pipe, PipeTransform } from '@angular/core';
import {ChapterPart} from "../models/chapter/chapterpart";
import {Practice} from "../models/practice/practice";

@Pipe({
  name: 'practiceBtnVisibility',
  standalone: true
})
export class PracticeButtonsVisibilityPipe implements PipeTransform {
  transform(chapterPartId: number, practices: Practice[]): {
    playVisible: boolean,
    pauseVisible: boolean,
    doneVisible: boolean,
    doneDisabled: boolean
  } {
    const practice :Practice | undefined = this.getPracticeByChapterPartId(chapterPartId, practices);
    const playVisible = practice?.state === 'NOT_STARTED' || practice?.state === 'PAUSE';
    const pauseVisible = practice?.state === 'IN_PROCESS';
    const doneVisible = practice?.state !== 'APPROVED';
    const doneDisabled = practice?.state === 'NOT_STARTED' || practice?.state === 'PAUSE';

    return { playVisible, pauseVisible, doneVisible, doneDisabled };
  }

  private getPracticeByChapterPartId(chapterPartId: number, practices: Practice[]): Practice | undefined {
    return practices.find(practice => practice.chapterPartId === chapterPartId);
  }
}