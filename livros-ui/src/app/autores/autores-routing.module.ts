import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AutorCadastroComponent } from './autor-cadastro/autor-cadastro.component';
import { AutoresPesquisaComponent } from './autores-pesquisa/autores-pesquisa.component';

const routes: Routes = [
  { path: '', component: AutoresPesquisaComponent },
  { path: 'new', component: AutorCadastroComponent },
  { path: 'edit/:codigo', component: AutorCadastroComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AutoresRoutingModule { }
