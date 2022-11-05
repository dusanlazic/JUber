const API_BASE_URL= 'http://localhost:8080';
const OAUTH2_REDIRECT_URI= 'http://localhost:3000/oauth2/redirect';


export const environment = {
  production: true,
  ACCESS_TOKEN: 'accessToken',
  GOOGLE_AUTH_URL: API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI,
  FACEBOOK_AUTH_URL: API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI,
};
