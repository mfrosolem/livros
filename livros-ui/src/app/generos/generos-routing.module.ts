import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { GeneroCadastroComponent } from './genero-cadastro/genero-cadastro.component';
import { GenerosPesquisaComponent } from './generos-pesquisa/generos-pesquisa.component';

const routes: Routes = [
  { path: '', component: GenerosPesquisaComponent },
  { path: 'new', component: GeneroCadastroComponent },
  { path: 'edit/:codigo', component: GeneroCadastroComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GenerosRoutingModule { }
