import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/internal/Observable";
import { getStudentAdditionalMaterialUrl, getStudentChapterUrl, getStudentChaptersUrl } from "src/app/enums/api-urls";
import { Chapter, ShortChapter } from "../models/chapter";
import { StateStudent } from "../models/student";
import { TokenStorageService } from "./token-storage.service";
import { User } from "../models/user";

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

