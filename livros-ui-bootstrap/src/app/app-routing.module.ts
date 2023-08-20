import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PaginaNaoEncontradaComponent } from './core/pagina-nao-encontrada.component';
import { PaginaNaoAutorizadaComponent } from './core/pagina-nao-autorizada.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'generos' },
  { path: 'pagina-nao-encontrada', component: PaginaNaoEncontradaComponent },
  { path: 'pagina-nao-autorizada', component: PaginaNaoAutorizadaComponent },
  {
    path: 'generos',
    loadChildren: () => import('./generos/generos.module').then(m => m.GenerosModule)
  },
  {
    path: 'editoras',
    loadChildren: () => import('./editoras/editoras.module').then(m => m.EditorasModule)
  },
  {
    path: 'autores',
    loadChildren: () => import('./autores/autores.module').then(m => m.AutoresModule)
  },
  {
    path: 'livros',
    loadChildren: () => import('./livros/livros.module').then(m => m.LivrosModule)
  },
  { path: '**', redirectTo: 'pagina-nao-encontrada' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
