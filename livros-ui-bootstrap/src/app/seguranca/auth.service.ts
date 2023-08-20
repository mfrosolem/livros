import { Injectable } from '@angular/core';
import { environment } from './../../environments/environment';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';

import * as CryptoJS from 'crypto-js';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  tokensRevokeUrl = environment.API + 'tokens/revoke';
  oauthTokenUrl = environment.API + 'oauth2/token';
  oauthAuthorizeUrl = environment.API + 'oauth2/authorize';
  jwtPayload: any;

  constructor(
    private http: HttpClient,
    private jwtHelper: JwtHelperService
  ) { 
      this.carregarToken();
  }

  login() {
    const state = this.gerarStringAleatoria(40);
    const codeVerifier = this.gerarStringAleatoria(128);

    localStorage.setItem('state', state);
    localStorage.setItem('codeVerifier', codeVerifier);

    const challengeMethod = 'S256';
    const codeChallenge = CryptoJS.SHA256(codeVerifier)
      .toString(CryptoJS.enc.Base64)
      .replace(/\+/g, "-")
      .replace(/\//g, "_")
      .replace(/=+$/, "");

    const redirectURI = encodeURIComponent(environment.oauthCallbackUrl);

    const clientId = 'livros-frontend';
    const scope = 'READ WRITE DELETE';
    const responseType = 'code';

    const params = [
      'response_type='+ responseType,
      'client_id='+ clientId,
      'scope='+ scope,
      'code_challenge='+ codeChallenge,
      'code_challenge_method='+ challengeMethod,
      'state='+ state,
      'redirect_uri='+ redirectURI
    ]

    window.location.href = this.oauthAuthorizeUrl + '?' + params.join('&');
  }

  obterNovoAccessTokenComCode(code: string, state: string): Promise<any> {
    const stateSalvo = localStorage.getItem('state');

    //Houve um problema (pode ser um ataque), não podemos constinuar com a requisição
    if(stateSalvo !== state) {
      return Promise.reject(null);
    }

    const codeVerifier = localStorage.getItem('codeVerifier')!;

    const payload = new HttpParams()
    .append('grant_type', 'authorization_code')
    .append('code', code)
    .append('redirect_uri', environment.oauthCallbackUrl)
    .append('code_verifier', codeVerifier);

    const headers = new HttpHeaders()
    .append('Content-Type', 'application/x-www-form-urlencoded')
    .append('Authorization', 'Basic bGl2cm9zLWZyb250ZW5kOmZyb250ZW5kMTIz');

    return firstValueFrom(
      this.http.post<any>(this.oauthTokenUrl, payload, { headers })
    )
    .then((response: any) => {
      this.armazenarToken(response['access_token']);
      this.armazenarRefreshToken(response['refresh_token']);

      localStorage.removeItem('state');
      localStorage.removeItem('codeVerifier');

      return Promise.resolve(null);
    }).catch((response: any) => {
      return Promise.resolve(null);
    });

  }


  obterNovoToken(): Promise<void> {
    const headers = new HttpHeaders()
      .append('Content-Type', 'application/x-www-form-urlencoded')
      .append('Authorization', 'Basic bGl2cm9zLWZyb250ZW5kOmZyb250ZW5kMTIz');

      console.log(headers)

    const payload = new HttpParams()
      .append('grant_type', 'refresh_token')
      .append('refresh_token', localStorage.getItem('refreshToken')!);

      console.log(payload)


    return firstValueFrom(
      this.http.post<any>(this.oauthTokenUrl, payload, { headers, withCredentials: true })
    )
      .then((response: any) => {
        this.armazenarToken(response['access_token']);
        this.armazenarRefreshToken(response['refresh_token']);
        console.log('Novo access token criado!');

        return Promise.resolve();
      })
      .catch(response => {
        console.error('Erro ao renovar token.', response);
        return Promise.resolve();
      });
  }



  isTokenInvalid() {
    const token = localStorage.getItem('token');
    return !token || this.jwtHelper.isTokenExpired(token);
  }

  hasPermission(permissao: string) {
    return this.jwtPayload && this.jwtPayload.authorities.includes(permissao);
  }

  hasAnyPermission(roles: any) {
    for (const role of roles) {
      if (this.hasPermission(role)) {
        return true;
      }
    }

    return false;
  }

  public armazenarToken(token: string) {
    this.jwtPayload = this.jwtHelper.decodeToken(token);
    localStorage.setItem('token', token);
  }

  public carregarToken() {
    const token = localStorage.getItem('token');

    if (token) {
      this.armazenarToken(token);
    }
  }

  removerToken() {
    localStorage.removeItem('token');
    this.jwtPayload = null;
  }

  logout() {
    this.removerToken();
    localStorage.clear();
    window.location.href = environment.API + 'logout?returnTo=' + environment.logoutRedirectToUrl;
  }

  private armazenarRefreshToken(refreshToken: string) {
    localStorage.setItem('refreshToken', refreshToken);
  }

  private gerarStringAleatoria(tamanho: number) {
    let resultado = '';
    //Chars que são URL safe
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for(let i = 0; i < tamanho; i++) {
      resultado += chars.charAt(Math.floor(Math.random() * chars.length));
    }

    return resultado;
  }
}
