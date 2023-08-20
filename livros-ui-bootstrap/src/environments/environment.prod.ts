export const environment = {
  production: true,
  API:  'http://localhost:8080/',
  tokenAllowedDomains: [/localhost:8080/],
  tokenDisallowedRoutes: [/\/oauth2\/token/],
  oauthCallbackUrl: 'http://local-livros.com:8000/authorized',
  logoutRedirectToUrl: 'http://local-livros.com:8000'
};
