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
    data: { roles: ['CONSULTAR_EDITORA'] } 
  },

  { 
    path: 'new', 
    canActivate: [AuthGuard], 
    component: EditoraCadastroComponent, 
    data: { roles: ['CADASTRAR_EDITORA'] } 
  },

  { 
    path: 'edit/:codigo', 
    canActivate: [AuthGuard], 
    component: EditoraCadastroComponent, 
    data: { roles: ['CADASTRAR_EDITORA'] } 
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EditorasRoutingModule { }
