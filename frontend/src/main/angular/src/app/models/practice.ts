export class Practice {
  id: number;
  chapterPartId: number;
  state: string;

  constructor(
    _id: number,
    _chapterPartId: number,
    _state: string
    ) {
      this.id = _id;
      this.chapterPartId = _chapterPartId;
      this.state = _state;
    }
    
}

