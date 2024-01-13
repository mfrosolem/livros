export const environment = {
  production: true,
  API:  'http://localhost:8080/',
  tokenAllowedDomains: [/localhost:8080/],
  tokenDisallowedRoutes: [/\/oauth2\/token/],
  oauthCallbackUrl: 'http://127.0.0.1:8000/authorized',
  logoutRedirectToUrl: 'http://127.0.0.1:8000'
};
