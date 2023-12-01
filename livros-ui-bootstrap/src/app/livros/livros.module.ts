import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';

import { FotoUploadComponent } from './foto-upload/foto-upload.component';
import { LivroCadastroComponent } from './livro-cadastro/livro-cadastro.component';
import { LivrosPesquisaComponent } from './livros-pesquisa/livros-pesquisa.component';
import { LivrosRoutingModule } from './livros-routing.module';
import { LivrosListComponent } from './livros-list/livros-list.component';


@NgModule({
  declarations: [
    LivrosPesquisaComponent,
    LivroCadastroComponent,
    FotoUploadComponent,
    LivrosListComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    PaginationModule.forRoot(),
    LivrosRoutingModule
  ]
})
export class LivrosModule { }
