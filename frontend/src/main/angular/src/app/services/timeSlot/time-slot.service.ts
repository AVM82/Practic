import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {catchError, Observable, of} from "rxjs";
import {ApiUrls} from "../../enums/api-urls";
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

    getAllAvailableTimeSlots(): Observable<Map<String, TimeSlot[]>> {
        return this.http.get<Map<String, TimeSlot[]>>(ApiUrls.TimeSlots).pipe(
            catchError(this.handleError<Map<String, TimeSlot[]>>(`get available time slots`)));
    }

    updateTimeslotAvailability(timeslotId: HttpParams): Observable<any> {
        return this.http.put<any>(ApiUrls.TimeSlots, timeslotId, { headers: this.headers }).pipe(
            catchError(this.handleError<any>(`update timeslot availability`)));
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
