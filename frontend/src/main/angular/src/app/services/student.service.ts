import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { TokenStorageService } from "./token-storage.service";
import { Chapter, NewStateChapter } from "../models/chapter";
import { StateStudent } from "../models/student";
import { getSetChapterStateUrl } from "../enums/api-urls";
import { CoursesService } from "./courses.service";

@Injectable({
    providedIn: 'root'
  })
  
export class StudentService {
    student!: StateStudent;

    constructor(
        private tokenStorageService: TokenStorageService,
        private coursesService: CoursesService,
        private http: HttpClient
    ) {
    }

    setStudent(student: StateStudent): void {
        this.student = student;
    }

    changeChapterState(chapter: Chapter, newState: string): void {
        this.http.put<NewStateChapter>(getSetChapterStateUrl(chapter.id, newState), {}).subscribe(state => {
            chapter.state = state.state;
            if (this.student.activeChapterNumber != state.activeChapterNumber) {
                this.student.activeChapterNumber = state.activeChapterNumber;
                if (state.activeChapterNumber == 0)
                    this.student.inactive = true
                else
                    this.coursesService.openStudentChapter(state.activeChapterNumber);
                this.tokenStorageService.saveMe();
            }
        })
    }
    
    
}

