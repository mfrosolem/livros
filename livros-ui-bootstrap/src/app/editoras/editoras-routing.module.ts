import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EditoraCadastroComponent } from './editora-cadastro/editora-cadastro.component';
import { EditorasPesquisaComponent } from './editoras-pesquisa/editoras-pesquisa.component';

const routes: Routes = [
  { path: '', component: EditorasPesquisaComponent },
  { path: 'new', component: EditoraCadastroComponent },
  { path: 'edit/:codigo', component: EditoraCadastroComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EditorasRoutingModule { }
