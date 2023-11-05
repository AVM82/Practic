import { Pipe, PipeTransform } from '@angular/core';
import {Practice} from "../models/practice";

@Pipe({
  name: 'practiceState',
  standalone: true
})
export class PracticeStatePipe implements PipeTransform {

  private stateTranslations: Record<string, string> = {
    'NOT_STARTED': 'не розпочато',
    'IN_PROCESS': 'в процесі',
    'PAUSE': 'на паузі',
    'READY_TO_REVIEW': 'на розгляді',
    'APPROVED': 'зараховано'
  };

  transform(chapterPartId: number, practices: Practice[]): string {
    if (practices) {
      const practice = practices.find(practice => practice.chapterPartId === chapterPartId);
      const state = practice ? practice.state : 'NOT_STARTED';
      return this.stateTranslations[state] || state;
    }
    return '';
  }

}
