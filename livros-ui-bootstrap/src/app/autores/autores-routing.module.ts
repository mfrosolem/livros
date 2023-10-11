import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AutorCadastroComponent } from './autor-cadastro/autor-cadastro.component';
import { AutoresPesquisaComponent } from './autores-pesquisa/autores-pesquisa.component';
import { AuthGuard } from '../seguranca/auth.guard';

const routes: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
    component: AutoresPesquisaComponent,
    data: { roles: ['CONSULTAR_AUTOR'] }
  },

  {
    path: 'new',
    canActivate: [AuthGuard],
    component: AutorCadastroComponent,
    data: { roles: ['CADASTRAR_AUTOR'] }
  },

  {
    path: 'edit/:codigo',
    canActivate: [AuthGuard],
    component: AutorCadastroComponent,
    data: { roles: ['CADASTRAR_AUTOR'] }
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AutoresRoutingModule { }
