import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LivroCadastroComponent } from './livro-cadastro/livro-cadastro.component';
import { LivrosPesquisaComponent } from './livros-pesquisa/livros-pesquisa.component';

const routes: Routes = [
  { path: '', component: LivrosPesquisaComponent },
  { path: 'new', component: LivroCadastroComponent },
  { path: 'edit/:codigo', component: LivroCadastroComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LivrosRoutingModule { }
