// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

const API_BASE_URL= 'http://localhost:8080';
const OAUTH2_REDIRECT_URI= 'http://localhost:3000/oauth2/redirect';

export const environment = {
  production: false,
  ACCESS_TOKEN: 'accessToken',
  GOOGLE_AUTH_URL: API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI,
  FACEBOOK_AUTH_URL: API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI,
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
