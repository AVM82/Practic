import { Pipe, PipeTransform } from '@angular/core';
import {Practice} from "../models/practice";
import { STATE_NOT_STARTED, STATE_PAUSE, STATE_IN_PROCESS, STATE_APPROVED, STATE_READY_TO_REVIEW } from '../enums/app-constans';

@Pipe({
  name: 'practiceBtnVisibility',
  standalone: true
})
export class PracticeButtonsVisibilityPipe implements PipeTransform {
  
  transform(practice: Practice): {
    playVisible: boolean,
    pauseVisible: boolean,
    doneVisible: boolean,
    doneDisabled: boolean
  } {
    const playVisible = practice?.state === STATE_NOT_STARTED 
          || practice?.state === STATE_PAUSE
          || practice?.state === STATE_READY_TO_REVIEW;
    const pauseVisible = practice?.state === STATE_IN_PROCESS;
    const doneVisible = practice?.state === STATE_IN_PROCESS;
    const doneDisabled = practice?.state === STATE_READY_TO_REVIEW;

    return { playVisible, pauseVisible, doneVisible, doneDisabled };
  }

}