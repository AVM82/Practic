import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {Subscription} from "rxjs";
import {TimerService} from "../../services/timer.service";
import {TranslocoRootModule} from "../../transloco-root.module";
import {CommonModule} from "@angular/common";

@Component({
    selector: 'app-timer',
    standalone: true,
    templateUrl: './timer.component.html',
    imports: [
        TranslocoRootModule,
        CommonModule
    ],
    styleUrls: ['./timer.component.css']
})
export class TimerComponent implements OnInit, OnDestroy {

    timerValue: { minutes: number, seconds: number } = { minutes: 0, seconds: 0 };
    @Output() timerValueChange = new EventEmitter<{ minutes: number, seconds: number }>();
    @Output() timerFinished = new EventEmitter<void>();
    private timerSubscription!: Subscription;

    constructor(private timerService: TimerService) {
    }

    ngOnInit(): void {
        this.timerSubscription = this.timerService.getTimer()
            .subscribe(seconds => {
                this.timerValue = {
                    minutes: Math.floor(seconds / 60),
                    seconds: seconds % 60
                };
                this.timerValueChange.emit(this.timerValue);

                if (this.timerValue.minutes === 0 && this.timerValue.seconds === 0) {
                    this.timerFinished.emit();
                }
            });
    }

    ngOnDestroy(): void {
        if (this.timerSubscription) {
            this.timerSubscription.unsubscribe();
        }
    }
}