import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CertificateInfo} from '../models/certificateInfo'
import {TokenStorageService} from "./token-storage.service";
import {ApiUrls} from "../enums/api-urls";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CertificateService {
  constructor(
      public http: HttpClient,
      public tokenStorageService: TokenStorageService) { }

  getCertificateInfo(): Observable<CertificateInfo> {
    return this.http.get<CertificateInfo>(ApiUrls.Certification);
  }
}
