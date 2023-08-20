import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, from, mergeMap } from "rxjs";

import { AuthService } from "./auth.service";

export class NotAuthenticatedError { }

@Injectable()
export class MoneyHttpInterceptor implements HttpInterceptor{

    constructor(private auth: AuthService) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!req.url.includes('/oauth2/token') && this.auth.isTokenInvalid()) {
            return from(this.auth.obterNovoToken())
                .pipe(
                    mergeMap(() => {
                        if (this.auth.isTokenInvalid()) {
                            throw new NotAuthenticatedError();
                        }

                        req = req.clone({
                            setHeaders: {
                                Authorization: `Bearer ${localStorage.getItem('token')}`
                            }
                        });

                        return next.handle(req);
                    })
                );
        }

        return next.handle(req);
    }
    
}
