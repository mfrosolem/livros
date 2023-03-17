import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';

import { SharedModule } from '../shared/shared.module';
import { EditoraCadastroComponent } from './editora-cadastro/editora-cadastro.component';
import { EditorasPesquisaComponent } from './editoras-pesquisa/editoras-pesquisa.component';
import { EditorasRoutingModule } from './editoras-routing.module';


@NgModule({
  declarations: [
    EditorasPesquisaComponent,
    EditoraCadastroComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,

    SharedModule,
    FormsModule,
    PaginationModule.forRoot(),
    EditorasRoutingModule
  ],
  exports: [
    EditorasRoutingModule
  ]
})
export class EditorasModule { }
