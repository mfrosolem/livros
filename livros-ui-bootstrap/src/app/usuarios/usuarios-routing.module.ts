import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from '../seguranca/auth.guard';
import { UsuariosPesquisaComponent } from './usuarios-pesquisa/usuarios-pesquisa.component';
import { UsuarioCadastroComponent } from './usuario-cadastro/usuario-cadastro.component';
import { AlterarSenhaComponent } from './alterar-senha/alterar-senha.component';

const routes: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
    component: UsuariosPesquisaComponent,
    data: { roles: ['CONSULTAR_USUARIOS_GRUPOS_PERMISSOES'] }
  },
  {
    path: 'new',
    canActivate: [AuthGuard],
    component: UsuarioCadastroComponent,
    data: { roles: ['CONSULTAR_USUARIOS_GRUPOS_PERMISSOES'] }
  },
  {
    path: 'edit/:codigo',
    canActivate: [AuthGuard],
    component: UsuarioCadastroComponent,
    data: { roles: ['CONSULTAR_USUARIOS_GRUPOS_PERMISSOES'] }
  },
  {
    path: 'alterar-senha',
    // canActivate: [AuthGuard],
    component: AlterarSenhaComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsuariosRoutingModule { }
