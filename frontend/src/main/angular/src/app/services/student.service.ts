import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { TokenStorageService } from "./token-storage.service";

@Injectable({
    providedIn: 'root'
  })
  
export class StudentService {
  
    constructor(
        private tokenStorageService: TokenStorageService,
        private http: HttpClient
    ) {
    }


    
    
    
}

