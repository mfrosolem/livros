import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GruposRoutingModule } from './grupos-routing.module';
import { GruposPesquisaComponent } from './grupos-pesquisa/grupos-pesquisa.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { SharedModule } from '../shared/shared.module';
import { GruposListComponent } from './grupos-list/grupos-list.component';
import { GrupoCadastroComponent } from './grupo-cadastro/grupo-cadastro.component';



@NgModule({
  declarations: [
    GruposPesquisaComponent,
    GruposListComponent,
    GrupoCadastroComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    PaginationModule.forRoot(),
    TooltipModule.forRoot(),
    SharedModule,

    GruposRoutingModule
  ]
})
export class GruposModule { }
