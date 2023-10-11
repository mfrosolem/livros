import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { GeneroCadastroComponent } from './genero-cadastro/genero-cadastro.component';
import { GenerosPesquisaComponent } from './generos-pesquisa/generos-pesquisa.component';
import { AuthGuard } from '../seguranca/auth.guard';

const routes: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
    component: GenerosPesquisaComponent,
    data: { roles: ['CONSULTAR_GENERO'] }
  },

  {
    path: 'new',
    canActivate: [AuthGuard],
    component: GeneroCadastroComponent,
    data: { roles: ['CADASTRAR_GENERO'] }
  },

  {
    path: 'edit/:codigo',
    canActivate: [AuthGuard],
    component: GeneroCadastroComponent,
    data: { roles: ['CADASTRAR_GENERO'] }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GenerosRoutingModule { }
