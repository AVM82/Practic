import {Injectable} from '@angular/core';
import {map, Observable, Subject, takeWhile, timer} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class TimerService {
    private timerSubject = new Subject<number>();

    startTimer(duration: number): void {
        timer(0, 1000)
            .pipe(
                map(seconds => duration - seconds),
                takeWhile(seconds => seconds >= 0)
            )
            .subscribe({
                    next: seconds => this.timerSubject.next(seconds),
                    error: () => {
                    },
                    complete: () => this.timerSubject.complete()
                }
            );
    }

    getTimer(): Observable<number> {
        return this.timerSubject.asObservable();
    }

    stopTimer(): void {
        this.timerSubject.unsubscribe();
    }
}
