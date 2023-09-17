import { Injectable } from '@angular/core';
import {Observable, Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class InfoMessagesService {

  private _message = new Subject<any>()

  showMessage(text: string, type: string) {
    this._message.next({text: text, type: type});
    setTimeout(() => this.clearMessage(), 5000);
  }

  clearMessage() {
    this._message.next({});
  }

  getMessage(): Observable<any> {
    return this._message.asObservable();
  }
}
