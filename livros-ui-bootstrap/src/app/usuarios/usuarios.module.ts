import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UsuariosRoutingModule } from './usuarios-routing.module';
import { UsuariosPesquisaComponent } from './usuarios-pesquisa/usuarios-pesquisa.component';
import { UsuariosListComponent } from './usuarios-list/usuarios-list.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [
    UsuariosPesquisaComponent,
    UsuariosListComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    PaginationModule.forRoot(),
    TooltipModule.forRoot(),
    SharedModule,

    UsuariosRoutingModule
  ]
})
export class UsuariosModule { }
