import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';

import { AutorCadastroComponent } from './autor-cadastro/autor-cadastro.component';
import { AutoresPesquisaComponent } from './autores-pesquisa/autores-pesquisa.component';
import { AutoresRoutingModule } from './autores-routing.module';


@NgModule({
  declarations: [
    AutoresPesquisaComponent,
    AutorCadastroComponent
  ],
  imports: [
    CommonModule,
    HttpClientModule,

    ReactiveFormsModule,
    NgbPaginationModule,

    AutoresRoutingModule
  ]
})
export class AutoresModule { }
