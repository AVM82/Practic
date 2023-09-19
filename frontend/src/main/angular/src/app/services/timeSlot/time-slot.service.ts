import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";
import {catchError, Observable, of} from "rxjs";
import {timeslotsUrl} from "../../enums/api-urls";
import {TimeSlot} from "../../models/timeSlot/time-slot";

@Injectable({
    providedIn: 'root'
})
export class TimeSlotService {

    constructor(private http: HttpClient,
                private router: Router) {
    }

    headers = new HttpHeaders({
        'Content-Type': 'application/json',
    });

    getAllAvailableTimeSlots(slug: string): Observable<Map<string, TimeSlot[]>> {
        return this.http.get<Map<string, TimeSlot[]>>(timeslotsUrl(slug)).pipe(
            catchError(this.handleError<Map<string, TimeSlot[]>>(`get available time slots`)));
    }

    updateTimeslotAvailability(timeslotId: number, slug: string): Observable<any> {
        return this.http.put<any>(timeslotsUrl(slug), timeslotId, {headers: this.headers}).pipe(
            catchError(this.handleError<any>(`update timeslot availability`)));
    }

    createNewTimeslots(slug: string): Observable<any> {
        return this.http.post<any>(timeslotsUrl(slug), {headers: this.headers}).pipe(
            catchError(this.handleError<any>(`create new timeslots`)));
    }

    /**
     * Handle Http operation that failed.
     * Let the app continue.
     *
     * @param operation - name of the operation that failed
     * @param result - optional value to return as the observable result
     */
    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {

            console.error(error);
            console.error(`${operation} failed: ${error.message}`);

            this.router.navigateByUrl('/404');

            return of(result as T);
        };
    }
}
