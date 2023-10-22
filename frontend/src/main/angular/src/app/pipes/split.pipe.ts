import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'split',
  pure: true,
  standalone: true
})
export class SplitPipe implements PipeTransform {
  transform(value: string, separator: string): string[] {
    return value.split(separator);
  }
}