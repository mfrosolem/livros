import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';

import { FotoUploadComponent } from './foto-upload/foto-upload.component';
import { LivroCadastroComponent } from './livro-cadastro/livro-cadastro.component';
import { LivrosPesquisaComponent } from './livros-pesquisa/livros-pesquisa.component';
import { LivrosRoutingModule } from './livros-routing.module';


@NgModule({
  declarations: [
    LivrosPesquisaComponent,
    LivroCadastroComponent,
    FotoUploadComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgbPaginationModule,
    LivrosRoutingModule
  ]
})
export class LivrosModule { }
