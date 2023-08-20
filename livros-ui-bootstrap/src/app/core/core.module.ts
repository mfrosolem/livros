import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ErrorHandlerService } from './error-handler.service';
import { NavbarComponent } from './navbar/navbar.component';
import { PaginaNaoEncontradaComponent } from './pagina-nao-encontrada.component';
import { AuthService } from '../seguranca/auth.service';
import { PaginaNaoAutorizadaComponent } from './pagina-nao-autorizada.component';



@NgModule({
  declarations: [
    NavbarComponent,
    PaginaNaoEncontradaComponent,
    PaginaNaoAutorizadaComponent
  ],
  imports: [
    CommonModule,
    RouterModule

  ],
  exports: [
    NavbarComponent
  ],
  providers: [
    ErrorHandlerService,
    AuthService
  ]
})
export class CoreModule { }
