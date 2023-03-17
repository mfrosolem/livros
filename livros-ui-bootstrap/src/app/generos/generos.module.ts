import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';

import { SharedModule } from '../shared/shared.module';
import { GeneroCadastroComponent } from './genero-cadastro/genero-cadastro.component';
import { GenerosPesquisaComponent } from './generos-pesquisa/generos-pesquisa.component';
import { GenerosRoutingModule } from './generos-routing.module';



@NgModule({
  declarations: [
    GenerosPesquisaComponent,
    GeneroCadastroComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    PaginationModule.forRoot(),
    SharedModule,

    GenerosRoutingModule
  ],
  exports: [
    GenerosRoutingModule
  ]
})
export class GenerosModule { }
