import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', pathMatch:'full', redirectTo: 'generos' },
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
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
