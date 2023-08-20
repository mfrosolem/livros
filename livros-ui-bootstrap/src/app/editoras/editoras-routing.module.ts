import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EditoraCadastroComponent } from './editora-cadastro/editora-cadastro.component';
import { EditorasPesquisaComponent } from './editoras-pesquisa/editoras-pesquisa.component';
import { AuthGuard } from '../seguranca/auth.guard';

const routes: Routes = [
  { 
    path: '', 
    canActivate: [AuthGuard], 
    component: EditorasPesquisaComponent, 
    data: { roles: ['ROLE_EDITORA_PESQUISAR'] } 
  },

  { 
    path: 'new', 
    canActivate: [AuthGuard], 
    component: EditoraCadastroComponent, 
    data: { roles: ['ROLE_EDITORA_CADASTRAR'] } 
  },

  { 
    path: 'edit/:codigo', 
    canActivate: [AuthGuard], 
    component: EditoraCadastroComponent, 
    data: { roles: ['ROLE_EDITORA_CADASTRAR'] } 
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EditorasRoutingModule { }
