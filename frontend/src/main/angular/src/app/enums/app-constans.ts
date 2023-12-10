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

export class ChapterState {
  static readonly NOT_STARTED = STATE_NOT_STARTED;
  static readonly IN_PROCESS = STATE_IN_PROCESS;
  static readonly PAUSE = STATE_PAUSE;
  static readonly DONE = STATE_DONE;
}

export class PracticeState {
  static readonly NOT_STARTED = STATE_NOT_STARTED;
  static readonly IN_PROCESS = STATE_IN_PROCESS;
  static readonly PAUSE = STATE_PAUSE;
  static readonly READY_TO_REVIEW = STATE_READY_TO_REVIEW;
  static readonly APPROVED = STATE_APPROVED;
}

export class ReportState {
  static readonly ANNOUNCED = STATE_ANNOUNCED;
  static readonly STARTED = STATE_STARTED;
  static readonly FINISHED = STATE_FINISHED;
  static readonly APPROVED = STATE_APPROVED;
  static readonly CANCELLED = STATE_CANCELLED;
  static readonly isActual = (state: string): boolean => state === this.ANNOUNCED || state === this.STARTED;
}

export const BUTTON_START = 'ПОЧАТИ';
export const BUTTON_FINISH = 'ЗАВЕРШИТИ';
export const BUTTON_PAUSE = 'ПАУЗА';
export const BUTTON_CONTINUE = 'ПРОДОВЖИТИ';
export const BUTTON_REPORT = 'ДОПОВІДЬ';
