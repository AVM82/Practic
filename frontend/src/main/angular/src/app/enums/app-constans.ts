import { HttpHeaders } from "@angular/common/http";
import {environment} from "../../enviroments/enviroment";

export const TOKEN_KEY = 'auth-token';
export const USER_KEY = 'auth-user';

export class AppConstants {
  private static API_BASE_URL = environment.apiBaseUrl;
  private static OAUTH2_URL = AppConstants.API_BASE_URL + "oauth2/authorization/";
  private static REDIRECT_URL = environment.redirectUrl;
  public static LINKEDIN_AUTH_URL = AppConstants.OAUTH2_URL + "linkedin" + AppConstants.REDIRECT_URL;
}


export const httpOptions = {
  headers: new HttpHeaders({'Content-Type':'application/json', 'Accept':'application/json'})
};


export class CreateMethod {
  static readonly INTERACTIVE = 'Interactive';
  static readonly FROM_FILE = 'FromFile'
  static readonly FROM_PROPERTIES = 'FromProperties';
}

export const CORRECT_ANSWERS_PERCENT = 75;

export const ROLE_ADMIN = 'ADMIN';
export const ROLE_STAFF = 'STAFF';
export const ROLE_COMRADE = 'COMRADE';
export const ROLE_MENTOR = 'MENTOR';
export const ROLE_GRADUATE = 'GRADUATE';
export const ROLE_STUDENT = 'STUDENT';
export const ROLE_GUEST = 'GUEST';

export class Roles {
  static readonly ADMIN = ROLE_ADMIN;
  static readonly STAFF = ROLE_STAFF;
  static readonly COMRADE = ROLE_COMRADE;
  static readonly MENTOR = ROLE_MENTOR;
  static readonly GRADUATE = ROLE_GRADUATE;
  static readonly STUDENT = ROLE_STUDENT;
  static readonly GUEST = ROLE_GUEST;  
  static readonly ADVANCED: string [] = [ROLE_ADMIN, ROLE_STAFF, ROLE_COMRADE, ROLE_MENTOR];
  static readonly isAdvanceRole = (role: string): boolean => Roles.ADVANCED.some(adv => adv === role);
}

export const ROLES = [ROLE_ADMIN, ROLE_STAFF, ROLE_COMRADE, ROLE_MENTOR, ROLE_STUDENT, ROLE_GUEST];

export const MANUALLY_CHANGED_ROLES = [ROLE_ADMIN, ROLE_STAFF, ROLE_COMRADE, ROLE_GUEST];


export const STATE_NOT_STARTED = 'NOT_STARTED';
export const STATE_IN_PROCESS = 'IN_PROCESS';
export const STATE_PAUSE = 'PAUSE';
export const STATE_DONE = 'DONE';
export const STATE_READY_TO_REVIEW = 'READY_TO_REVIEW';
export const STATE_APPROVED = 'APPROVED';
export const STATE_CANCELLED = 'CANCELLED';
export const STATE_FINISHED = 'FINISHED';
export const STATE_STARTED = 'STARTED';
export const STATE_ANNOUNCED = 'ANNOUNCED';
export const STATE_QUIZ_RESULT = "STATE_QUIZ_RESULT";


export class ChapterState {
  static readonly NOT_STARTED = STATE_NOT_STARTED;
  static readonly IN_PROCESS = STATE_IN_PROCESS;
  static readonly PAUSE = STATE_PAUSE;
  static readonly DONE = STATE_DONE;
  static readonly WEIGHT_THEORY = 0.25;
  static readonly WEIGHT_PRACTIC = 0.50;
  static readonly WEIGHT_REPORT = 0.15;
  static readonly WEIGHT_QUIZ = 0.10;
  static readonly countAveragePercent = (theory: number, practic: number, report: number, quiz: number): number =>
        Math.floor(theory * ChapterState.WEIGHT_THEORY + practic * ChapterState.WEIGHT_PRACTIC
          + report * ChapterState.WEIGHT_REPORT + quiz * ChapterState.WEIGHT_QUIZ)
}

export class PracticeState {
  static readonly NOT_STARTED = STATE_NOT_STARTED;
  static readonly IN_PROCESS = STATE_IN_PROCESS;
  static readonly PAUSE = STATE_PAUSE;
  static readonly READY_TO_REVIEW = STATE_READY_TO_REVIEW;
  static readonly APPROVED = STATE_APPROVED;
  static readonly PERCENT = new Map<string, number>([
    [PracticeState.NOT_STARTED, 0],
    [PracticeState.IN_PROCESS, 33],
    [PracticeState.PAUSE, 33],
    [PracticeState.READY_TO_REVIEW, 70],
    [PracticeState.APPROVED, 100]
  ]);
}

export class ReportState {
  static readonly ANNOUNCED = STATE_ANNOUNCED;
  static readonly STARTED = STATE_STARTED;
  static readonly FINISHED = STATE_FINISHED;
  static readonly APPROVED = STATE_APPROVED;
  static readonly CANCELLED = STATE_CANCELLED;
  static readonly isActual = (state: string): boolean => state === this.ANNOUNCED || state === this.STARTED;
  static readonly PERCENT = new Map<string, number>([
    [ReportState.ANNOUNCED, 10],
    [ReportState.STARTED, 50],
    [ReportState.FINISHED, 80],
    [ReportState.APPROVED, 100],
    [ReportState.CANCELLED, 0]
  ]);
}

export const BUTTON_START = 'ПОЧАТИ';
export const BUTTON_FINISH = 'ЗАВЕРШИТИ';
export const BUTTON_PAUSE = 'ПАУЗА';
export const BUTTON_CONTINUE = 'ПРОДОВЖИТИ';
export const BUTTON_REPORT = 'ДОПОВІДЬ';
export const BUTTON_TEST = 'ТЕСТ';
export const BUTTON_RESULT_TEST = 'ПОДИВИТИСЬ ТЕСТ';

export const LEVEL_COLORS = ['#84C984', '#D86D6D', '#CED069', '#6565A3']

export const REQUIRED_REPORT_COUNT: number = 1;
export const DAYS_AHEAD_REPORT_ANNOUNCE = 7;
export const MAX_REPORT_COUNT_PER_DAY = 3;
