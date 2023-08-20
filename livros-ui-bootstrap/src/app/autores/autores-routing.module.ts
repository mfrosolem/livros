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
    data: { roles: ['ROLE_AUTOR_PESQUISAR'] }
  },

  {
    path: 'new',
    canActivate: [AuthGuard],
    component: AutorCadastroComponent,
    data: { roles: ['ROLE_AUTOR_CADASTRAR'] }
  },

  {
    path: 'edit/:codigo',
    canActivate: [AuthGuard],
    component: AutorCadastroComponent,
    data: { roles: ['ROLE_AUTOR_CADASTRAR'] }
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AutoresRoutingModule { }
