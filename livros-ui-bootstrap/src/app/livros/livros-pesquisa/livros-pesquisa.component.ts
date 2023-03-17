import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { EMPTY, switchMap, take } from 'rxjs';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { ILivro } from './../../core/models/model';
import { ConfirmModalService } from './../../shared/confirm-modal.service';
import { ToastService } from './../../shared/toast.service';
import { LivroFilter, LivroService } from './../livro.service';

@Component({
  selector: 'app-livros-pesquisa',
  templateUrl: './livros-pesquisa.component.html',
  styleUrls: ['./livros-pesquisa.component.css'],
  preserveWhitespaces: true
})
export class LivrosPesquisaComponent implements OnInit {

  livros: ILivro[] = [];
  form: FormGroup = this.formBuilder.group({
    titulo: []
  });

  quantidadeItemPagina: number = 5;
  page?: number = 1;
  totalRegistros = 0;
  totalPages = 0;

  constructor(
    private formBuilder: FormBuilder,
    private livroService: LivroService,
    private errorHandlerService: ErrorHandlerService,
    private toastService: ToastService,
    private confirmModalService: ConfirmModalService,
    private router: Router,
    private route: ActivatedRoute,
    private title: Title
  ) { }

  ngOnInit(): void {
    this.title.setTitle('Pesquisa Livros');
    this.onSearch();
  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  onEdit(livro: ILivro) {
    this.router.navigate(['edit', livro.id], { relativeTo: this.route });
  }

  onSearch(page: number = 0) {
    const filterLivro = new LivroFilter(page, this.quantidadeItemPagina, this.form.value.titulo);
    this.livroService.findByFilter(filterLivro)
      .subscribe({
        next: (dados) => {
          this.livros = dados.livros;
          this.totalRegistros = dados.totalElements;
          this.totalPages = dados.totalPages;
        },
        error: (falha) => {
          this.errorHandlerService.handle(falha);
        },
      });
  }

  pageChanged(event: PageChangedEvent): void {
    this.page = event.page;
    this.onSearch(this.page - 1);
  }

  confirmDelete(livro: ILivro) {

    const resultado$ =
      this.confirmModalService.showConfirm('Confirmação', `Deseja excluir ${livro.titulo}`);

    resultado$.asObservable()
      .pipe(
        take(1),
        switchMap(resultConfirm => resultConfirm ? this.livroService.remove(livro.id) : EMPTY)
      )
      .subscribe(
        {
          next: () => {
            this.toastService.showSuccessToast('Livro excluído com sucesso');
            this.onRefreshPagina();
          },
          error: (erro) => {
            this.errorHandlerService.handle(erro);
          }
        }
      );
  }


  private onRefreshPagina() {
    this.page = 1;
    this.totalRegistros = 0;
    this.totalPages = 0;
    this.onSearch();
  }

}
