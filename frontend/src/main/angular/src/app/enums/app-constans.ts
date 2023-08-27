export class AppConstants {
  private static API_BASE_URL = "http://restapishpp.eu-north-1.elasticbeanstalk.com/";
  private static OAUTH2_URL = AppConstants.API_BASE_URL + "oauth2/authorization/";
  private static REDIRECT_URL = "?redirect_uri=http://restapishpp.eu-north-1.elasticbeanstalk.com/login";
  public static LINKEDIN_AUTH_URL = AppConstants.OAUTH2_URL + "linkedin" + AppConstants.REDIRECT_URL;
}
