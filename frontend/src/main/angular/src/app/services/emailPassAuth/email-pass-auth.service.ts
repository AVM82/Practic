import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ApiUrls} from "../../enums/api-urls";

@Injectable({
  providedIn: 'root'
})
export class EmailPassAuthService {

  constructor(private http: HttpClient) { }

  postData(name:string, email:string, password: string):  Observable<any>{
    const headers = new HttpHeaders({'Content-Type':'application/json'});
    const body = {
      name: name,
      email: email,
      password: password
    };
  return this.http.post(ApiUrls.EmailPassAuth,body,{headers:headers})  
  }
}
