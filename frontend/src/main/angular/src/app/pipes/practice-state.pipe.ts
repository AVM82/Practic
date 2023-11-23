import { Pipe, PipeTransform } from '@angular/core';
import {Practice} from "../models/practice";

@Pipe({
  name: 'StatePipe',
  standalone: true
})
export class StatePipe implements PipeTransform {

  private stateTranslations: Record<string, string> = {
    'NOT_STARTED': 'не розпочато',
    'IN_PROCESS': 'в процесі',
    'PAUSE': 'на паузі',
    'READY_TO_REVIEW': 'на розгляді',
    'APPROVED': 'зараховано',
    'DONE': 'пройдено'
  };

  transform(state: string): string {
      return this.stateTranslations[state];
  }


}
