import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PermissoesRoutingModule } from './permissoes-routing.module';
import { PermissoesPesquisaComponent } from './permissoes-pesquisa/permissoes-pesquisa.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [
    PermissoesPesquisaComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    PaginationModule.forRoot(),
    TooltipModule.forRoot(),
    SharedModule,

    PermissoesRoutingModule
  ],
  exports: [
    PermissoesRoutingModule
  ]
})
export class PermissoesModule { }
