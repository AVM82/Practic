import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiUrls } from "../enums/api-urls";

@Injectable({
  providedIn: 'root'
})
export class EmailPassAuthService {

  constructor(private http: HttpClient) { }

  postData(name: string, email: string, password: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = {
      name: name,
      email: email,
      password: password
    };

    return this.http.post(ApiUrls.EmailPassAuth, body, { headers: headers })
  }

  sendEmailForGetSecretCode(email: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    const apiUrl = `${ApiUrls.SendSecretCode + "?email="}${email}`;

    return this.http.post(apiUrl, headers);
  };

  sendSecretCodeForPasswordReset(seretCode: string, email: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = {
      code: seretCode,
      email: email
    };

    return this.http.post(ApiUrls.MatchCode, body, { headers: headers })
  };


  sendNewPassword(seretCode: string, email: string, newPass: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = {
      code: seretCode,
      email: email,
      newPassword: newPass
    };

    return this.http.patch(ApiUrls.ResetPassword, body, { headers: headers })
  };

}
