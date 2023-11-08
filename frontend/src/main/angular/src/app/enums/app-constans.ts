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
  static INTERACTIVE = 'Interactive';
  static FROM_FILE = 'FromFile'
  static FROM_PROPERTIES = 'FromProperties';
}

export const ROLE_ADMIN = 'ADMIN';
export const ROLE_COLLABORATOR = 'COLLABORATOR';
export const ROLE_COMRADE = 'COMRADE';
export const ROLE_MENTOR = 'MENTOR';
export const ROLE_STUDENT = 'STUDENT';
export const ROLE_GUEST = 'GUEST';

export class Roles {
  static ADMIN = ROLE_ADMIN;
  static COLLABORATOR = ROLE_COLLABORATOR;
  static COMRADE = ROLE_COMRADE;
  static MENTOR = ROLE_MENTOR;
  static STUDENT = ROLE_STUDENT;
  static GUEST = ROLE_GUEST;  
  static ADVANCED: string [] = [ROLE_ADMIN, ROLE_COLLABORATOR, ROLE_COMRADE, ROLE_MENTOR];
}

export const ROLES = [ROLE_ADMIN, ROLE_COLLABORATOR, ROLE_COMRADE, ROLE_MENTOR, ROLE_STUDENT, ROLE_GUEST];

export const MANUALLY_CHANGED_ROLES = [ROLE_ADMIN, ROLE_COLLABORATOR, ROLE_COMRADE, ROLE_GUEST];
