import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard  {

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

     if (this.auth.isTokenInvalid()) {
        console.log('Navegação com access token inválido. Obtendo novo token...');

        return this.auth.obterNovoToken()
          .then(() => {
            if (this.auth.isTokenInvalid()) {
              this.auth.login();
              return false;
            }
  
            return this.podeAcessarRota(next.data['roles']);
          });
      } 

      return this.podeAcessarRota(next.data['roles']);
  }

  podeAcessarRota(roles: String[]): boolean {
    if (roles && !this.auth.hasAnyPermission(roles)) {
      this.router.navigate(['/pagina-nao-autorizada']);
      return false;
    }
    return true;
  }
  
}
