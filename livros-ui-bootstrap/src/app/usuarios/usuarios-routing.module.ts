import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../seguranca/auth.guard';
import { UsuariosPesquisaComponent } from './usuarios-pesquisa/usuarios-pesquisa.component';

const routes: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
    component: UsuariosPesquisaComponent,
    data: { roles: ['CONSULTAR_USUARIOS_GRUPOS_PERMISSOES'] }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsuariosRoutingModule { }
