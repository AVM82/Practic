export class SubSubChapter {
  id: number;
  number: number;
  name: string;
  refs: string | undefined;

  constructor(id: number, number: number, name: string, refs: string) {
    this.id = id;
    this.number = number;
    this.name = name;
    this.refs = refs;
  }
}
