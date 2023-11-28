import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../seguranca/auth.guard';
import { PermissoesPesquisaComponent } from './permissoes-pesquisa/permissoes-pesquisa.component';

const routes: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
    component: PermissoesPesquisaComponent,
    data: { roles: ['CONSULTAR_USUARIOS_GRUPOS_PERMISSOES'] }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PermissoesRoutingModule { }
