import { InjectionToken } from '@angular/core';
import * as _moment from 'moment';
import { Moment } from 'moment';
import { NgxMatDateAdapter } from '@angular-material-components/datetime-picker';
import * as i0 from "@angular/core";
/** Configurable options for {@see MomentDateAdapter}. */
export interface NgxMatMomentDateAdapterOptions {
    /**
     * When enabled, the dates have to match the format exactly.
     * See https://momentjs.com/guides/#/parsing/strict-mode/.
     */
    strict?: boolean;
    /**
     * Turns the use of utc dates on or off.
     * Changing this will change how Angular Material components like DatePicker output dates.
     * {@default false}
     */
    useUtc?: boolean;
}
/** InjectionToken for moment date adapter to configure options. */
export declare const NGX_MAT_MOMENT_DATE_ADAPTER_OPTIONS: InjectionToken<NgxMatMomentDateAdapterOptions>;
/** @docs-private */
export declare function NGX_MAT_MOMENT_DATE_ADAPTER_OPTIONS_FACTORY(): NgxMatMomentDateAdapterOptions;
/** Adapts Moment.js Dates for use with Angular Material. */
export declare class NgxMatMomentAdapter extends NgxMatDateAdapter<Moment> {
    private _options?;
    private _localeData;
    constructor(dateLocale: string, _options?: NgxMatMomentDateAdapterOptions);
    setLocale(locale: string): void;
    getYear(date: Moment): number;
    getMonth(date: Moment): number;
    getDate(date: Moment): number;
    getDayOfWeek(date: Moment): number;
    getMonthNames(style: 'long' | 'short' | 'narrow'): string[];
    getDateNames(): string[];
    getDayOfWeekNames(style: 'long' | 'short' | 'narrow'): string[];
    getYearName(date: Moment): string;
    getFirstDayOfWeek(): number;
    getNumDaysInMonth(date: Moment): number;
    clone(date: Moment): Moment;
    createDate(year: number, month: number, date: number): Moment;
    today(): Moment;
    parse(value: any, parseFormat: string | string[]): Moment | null;
    format(date: Moment, displayFormat: string): string;
    addCalendarYears(date: Moment, years: number): Moment;
    addCalendarMonths(date: Moment, months: number): Moment;
    addCalendarDays(date: Moment, days: number): Moment;
    toIso8601(date: Moment): string;
    /**
     * Returns the given value if given a valid Moment or null. Deserializes valid ISO 8601 strings
     * (https://www.ietf.org/rfc/rfc3339.txt) and valid Date objects into valid Moments and empty
     * string into null. Returns an invalid date for all other values.
     */
    deserialize(value: any): Moment | null;
    isDateInstance(obj: any): boolean;
    isValid(date: Moment): boolean;
    invalid(): Moment;
    getHour(date: _moment.Moment): number;
    getMinute(date: _moment.Moment): number;
    getSecond(date: _moment.Moment): number;
    setHour(date: _moment.Moment, value: number): void;
    setMinute(date: _moment.Moment, value: number): void;
    setSecond(date: _moment.Moment, value: number): void;
    /** Creates a Moment instance while respecting the current UTC settings. */
    private _createMoment;
    static ɵfac: i0.ɵɵFactoryDeclaration<NgxMatMomentAdapter, [{ optional: true; }, { optional: true; }]>;
    static ɵprov: i0.ɵɵInjectableDeclaration<NgxMatMomentAdapter>;
}
