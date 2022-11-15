// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

const API_BASE_URL= 'http://localhost:8080';
const OAUTH2_REDIRECT_URI_LOGIN= 'http://localhost:3000/oauth2/redirect-login';
const OAUTH2_REDIRECT_URI_REGISTER= 'http://localhost:3000/oauth2/redirect-register';

export const environment = {
  production: false,
  ACCESS_TOKEN: 'accessToken',

  API_BASE_URL: API_BASE_URL,
  OAUTH2_REDIRECT_URI_LOGIN: OAUTH2_REDIRECT_URI_LOGIN,
  OAUTH2_REDIRECT_URI_REGISTER: OAUTH2_REDIRECT_URI_REGISTER,

  GOOGLE_AUTH_URL_LOGIN: API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI_LOGIN,
  FACEBOOK_AUTH_URL_LOGIN: API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI_LOGIN,

  GOOGLE_AUTH_URL_REGISTER: API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI_REGISTER,
  FACEBOOK_AUTH_URL_REGISTER: API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI_REGISTER,
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
