import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbPaginationModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';

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
    NgbPaginationModule,
    SharedModule,
    NgbTooltipModule,

    GenerosRoutingModule
  ],
  exports: [
    GenerosRoutingModule
  ]
})
export class GenerosModule { }