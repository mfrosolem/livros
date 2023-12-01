import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';

import { AutorCadastroComponent } from './autor-cadastro/autor-cadastro.component';
import { AutoresPesquisaComponent } from './autores-pesquisa/autores-pesquisa.component';
import { AutoresRoutingModule } from './autores-routing.module';
import { AutoresListComponent } from './autores-list/autores-list.component';



@NgModule({
  declarations: [
    AutoresPesquisaComponent,
    AutorCadastroComponent,
    AutoresListComponent
  ],
  imports: [
    CommonModule,

    ReactiveFormsModule,
    FormsModule,
    PaginationModule.forRoot(),

    AutoresRoutingModule
  ]
})
export class AutoresModule { }
