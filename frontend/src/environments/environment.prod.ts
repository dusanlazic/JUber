const API_BASE_URL= 'http://localhost:8080';
const API_SOCKET_URL= 'http://localhost:8080/ws';
const OAUTH2_REDIRECT_URI_LOGIN= 'http://localhost:3000/oauth2/redirect-login';
const OAUTH2_REDIRECT_URI_REGISTER= 'http://localhost:3000/oauth2/redirect-register';


export const environment = {
  production: true,
  TOKEN_EXPIRATION: 'tokenExpiration',

  API_BASE_URL: API_BASE_URL,
  API_SOCKET_URL: API_SOCKET_URL,
  OAUTH2_REDIRECT_URI_LOGIN: OAUTH2_REDIRECT_URI_LOGIN,
  OAUTH2_REDIRECT_URI_REGISTER: OAUTH2_REDIRECT_URI_REGISTER,

  GOOGLE_AUTH_URL_LOGIN: API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI_LOGIN,
  FACEBOOK_AUTH_URL_LOGIN: API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI_LOGIN,

  GOOGLE_AUTH_URL_REGISTER: API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI_REGISTER,
  FACEBOOK_AUTH_URL_REGISTER: API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI_REGISTER,
};
