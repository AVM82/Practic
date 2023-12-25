import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ApiUrls} from "../enums/api-urls";
import {FormReport, FrontReport, Report} from "../models/report";
import { REQUIRED_REPORT_COUNT, ReportState, STATE_CANCELLED } from '../enums/app-constans';
import { Chapter } from '../models/chapter';


@Injectable({
    providedIn: 'root'
})
export class ReportService {

    constructor(private http: HttpClient) {
    }

    static insertReport(reports: Report[], report: Report): void {
        reports.push(report);
        reports.sort((a,b) => a.date < b.date ? -1 : 0);
    }

    fillFreeDateFrom(slug: string, arr: boolean[], date: Date): void {
        console.log('free date from: ', date.toISOString().split('T')[0])
        this.http.get<boolean[]>(ApiUrls.Reports + 'freeDates/' + slug + '/' 
                + date.toISOString().split('T')[0]).subscribe(days => {
            arr.length = 0;
            days.forEach(day => arr.push(day));
        })
    }

    createReport(newReport: FormReport, isStudent: boolean, myId: number, slug: string) : void {
        this.http.post<Report>(ApiUrls.Reports + slug, new FrontReport(newReport, isStudent, myId)).subscribe( report => {
            if (report && report.id != 0) {
                ReportService.insertReport(newReport.chapter.reports, report);
                if (newReport.chapter.myReports)
                    ReportService.insertReport(newReport.chapter.myReports, report);
            }
        });
    }

    static refreshMyReports(chapter: Chapter): void {
        if (chapter.myReports)
            chapter.reports.forEach(report => {
                if (report.studentChapterId === chapter.id) {
                    let i = chapter.myReports.findIndex(myReport => myReport.id === report.id);
                    if (i < 0)
                        this.insertReport(chapter.myReports, report)
                    else
                        chapter.myReports[i] = report;
                }
            })
    }

    refreshReport(report: Report, chapter: Chapter): void {
        this.http.get<Report>(ApiUrls.Reports + report.id).subscribe(fresh => {
            if (report.chapterNumber !== fresh.chapterNumber)
                ReportService.deleteReport(chapter.reports, report); 
            else 
                Object.assign(report, fresh);
        });
    }

    refreshChapter(chapter: Chapter, slug: string): void {
        this.http.get<Report[]>(ApiUrls.Reports + slug + '/' + chapter.number).subscribe(fresh => {
            chapter.reports = fresh;
            ReportService.refreshMyReports(chapter);
        });
    }

    changeState(report: Report, newState: string) : void {
        this.http.patch<Report>(ApiUrls.Reports + report.id + '/' + newState, {}).subscribe(fresh =>
            Object.assign(report,fresh));
    }

    updateReport(report: Report, changed: FormReport, isStudent: boolean, myId: number, fromChapter: Chapter): void {
        this.http.put<Report>(ApiUrls.Reports + report.id, new FrontReport(changed, isStudent, myId))
            .subscribe(fresh => {
                if (fresh && fresh.id != 0) {
                    if (report.chapterNumber !== fresh.chapterNumber)
                        ReportService.moveReport(report, fromChapter, changed.chapter)
                    Object.assign(report, fresh);
                }
            });
    }

    private static moveReport(report: Report, fromChapter: Chapter, toChapter: Chapter): void {
        this.deleteReport(fromChapter.reports, report);
        this.insertReport(toChapter.reports, report);
    }

    private static deleteReport(reports: Report[], report: Report): void {
        reports.splice(reports.findIndex(r => r.id === report.id), 1);
    }

    cancelReport(report: Report, chapter: Chapter): void {
        this.http.patch<Report>(ApiUrls.Reports + report.id + '/' + STATE_CANCELLED, {}).subscribe(fresh => {
            Object.assign(report, fresh);
            ReportService.deleteReport(chapter.reports, report);
        })
    }

    updateReportLikes(report: Report): void {
        this.http.patch<Report>(ApiUrls.Reports + report.id + '/like', {}).subscribe(fresh => Object.assign(report, fresh));
    }

    reportsSubmitted(reports: Report[]): boolean {
        return reports.filter(report => report.state === ReportState.APPROVED).length == REQUIRED_REPORT_COUNT;
    }

}
