import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';

import { SharedModule } from '../shared/shared.module';
import { GeneroCadastroComponent } from './genero-cadastro/genero-cadastro.component';
import { GenerosPesquisaComponent } from './generos-pesquisa/generos-pesquisa.component';
import { GenerosRoutingModule } from './generos-routing.module';
import { GenerosListComponent } from './generos-list/generos-list.component';



@NgModule({
  declarations: [
    GenerosPesquisaComponent,
    GeneroCadastroComponent,
    GenerosListComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    PaginationModule.forRoot(),
    SharedModule,

    GenerosRoutingModule
  ]

})
export class GenerosModule { }
