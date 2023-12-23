import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ApiUrls} from "../enums/api-urls";
import {CertificateInfo} from "../models/certificateInfo";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private http: HttpClient) { }

  getCertificateInfo(studentId: number): Observable<CertificateInfo> {
    return this.http.get<CertificateInfo>(ApiUrls.Certification + studentId)
  }

  postCertificateInfo(data: CertificateInfo, studentId: number): void{
    this.http.post(ApiUrls.Certification + "request/" + studentId, data).subscribe();
  }
}
