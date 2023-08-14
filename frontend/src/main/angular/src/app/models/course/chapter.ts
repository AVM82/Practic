export class Chapter {
  id: number;
  number: number;
  name: string;
  isVisible: boolean;

  constructor(id: number, number: number, name: string) {
    this.id = id;
    this.number = number;
    this.name = name;
    this.isVisible = false;
  }
}
