import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JwtHelperService, JwtModule } from '@auth0/angular-jwt';

import { environment } from './../../environments/environment';
import { SegurancaRoutingModule } from './seguranca-routing.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { MoneyHttpInterceptor } from './money-http-interceptor';
import { AuthGuard } from './auth.guard';
import { AuthorizedComponent } from './authorized/authorized.component';

export function tokenGetter(): string {
  return localStorage.getItem('token')!;
}


@NgModule({
  declarations: [
    AuthorizedComponent
  ],
  imports: [
    CommonModule,

    JwtModule.forRoot({
      config: {
        tokenGetter,
        allowedDomains: environment.tokenAllowedDomains,        //['localhost:8080'],
        disallowedRoutes: environment.tokenDisallowedRoutes    //['http://localhost:8080/oauth2/token']
      }
    }),

    SegurancaRoutingModule
  ],

  providers: [
    JwtHelperService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: MoneyHttpInterceptor,
      multi: true
    },
    AuthGuard
  ]
})
export class SegurancaModule { }
