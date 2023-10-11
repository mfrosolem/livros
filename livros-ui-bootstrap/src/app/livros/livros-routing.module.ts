import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { FotoUploadComponent } from './foto-upload/foto-upload.component';
import { LivroCadastroComponent } from './livro-cadastro/livro-cadastro.component';
import { LivrosPesquisaComponent } from './livros-pesquisa/livros-pesquisa.component';
import { AuthGuard } from '../seguranca/auth.guard';

const routes: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
    component: LivrosPesquisaComponent,
    data: { roles: ['CONSULTAR_LIVRO'] }
  },

  {
    path: 'new',
    canActivate: [AuthGuard],
    component: LivroCadastroComponent,
    data: { roles: ['CADASTRAR_LIVRO'] }
  },

  {
    path: 'edit/:codigo',
    canActivate: [AuthGuard],
    component: LivroCadastroComponent,
    data: { roles: ['CADASTRAR_LIVRO'] }
  },

  {
    path: 'edit/:codigo/foto',
    canActivate: [AuthGuard],
    component: FotoUploadComponent,
    data: { roles: ['CADASTRAR_LIVRO'] }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LivrosRoutingModule { }
