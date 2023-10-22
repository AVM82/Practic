import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'slugify',
  standalone: true
})
export class SlugifyPipe implements PipeTransform {

  transform(value: string, toSlug: boolean = true): string {
    if (!value) {
      return '';
    }

    if (toSlug) {
      return value.replace(/ /g, '-');
    } else {
      return value.replace(/-/g, ' ');
    }
  }
}
