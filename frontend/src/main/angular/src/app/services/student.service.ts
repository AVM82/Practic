import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { TokenStorageService } from "./token-storage.service";
import { Chapter, NewStateChapter } from "../models/chapter";
import { StateStudent } from "../models/student";
import { ApiUrls, getSetChapterStateUrl } from "../enums/api-urls";
import { CoursesService } from "./courses.service";
import { Practice } from "../models/practice";
import { ChapterPart } from "../models/chapterpart";
import { STATE_IN_PROCESS, STATE_PAUSE, STATE_READY_TO_REVIEW } from "../enums/app-constans";

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
            if (state.state === STATE_PAUSE)
                chapter.parts.forEach(part => {
                    if (part.practice.state === STATE_IN_PROCESS || part.practice.state === STATE_READY_TO_REVIEW)
                        this.changePracticeState(chapter.number, part, STATE_PAUSE);
                });
            this.coursesService.changeChapterState(chapter.number, chapter.state);
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
    
    changePracticeState(chapterN: number, part: ChapterPart, newState: string): void {
        this.http.put<Practice>(ApiUrls.PracticeStates + part.practice.id + `/` + newState, {}).subscribe(fresh => {
            part.practice.state = fresh.state;
            this.coursesService.changePracticeState(chapterN, fresh);
        })
    }


}

