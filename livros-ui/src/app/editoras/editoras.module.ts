import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';

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
    HttpClientModule,

    SharedModule,
    NgbPaginationModule,
    EditorasRoutingModule
  ],
  exports: [
    EditorasRoutingModule
  ]
})
export class EditorasModule { }
