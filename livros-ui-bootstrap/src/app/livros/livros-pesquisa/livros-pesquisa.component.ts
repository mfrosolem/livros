import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { EMPTY, Observable, catchError, first, of, switchMap, take, tap } from 'rxjs';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { ConfirmModalService } from './../../shared/confirm-modal.service';
import { ToastService } from './../../shared/toast.service';
import { LivroFilter, LivroService } from './../livro.service';
import { AuthService } from 'src/app/seguranca/auth.service';
import { Livro } from '../../core/models/livro/livro';
import { LivroPage } from '../../core/models/livro/livro-page';

@Component({
  selector: 'app-livros-pesquisa',
  templateUrl: './livros-pesquisa.component.html',
  styleUrls: ['./livros-pesquisa.component.css'],
  preserveWhitespaces: true
})
export class LivrosPesquisaComponent implements OnInit {

  naoPodeRemover = true;
  naoPodeEditar = true;

  livros$: Observable<LivroPage> | null = null;
  form: FormGroup = this.formBuilder.group({
    titulo: []
  });

  quantidadeItemPagina: number = 10;
  page?: number = 1;
  totalElements = 0;
  totalPages = 0;

  constructor(
    private formBuilder: FormBuilder,
    private livroService: LivroService,
    private errorHandlerService: ErrorHandlerService,
    private toastService: ToastService,
    private confirmModalService: ConfirmModalService,
    private router: Router,
    private route: ActivatedRoute,
    private title: Title,
    private auth: AuthService
  ) { 
    this.naoPodeRemover = this.naoTemPermissao('REMOVER_LIVRO');
    this.naoPodeEditar = this.naoTemPermissao('CADASTRAR_LIVRO');
    this.onSearch();
  }

  ngOnInit(): void {
    this.title.setTitle('Pesquisa Livros');    
  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  onEdit(livro: Livro) {
    this.router.navigate(['edit', livro.id], { relativeTo: this.route });
  }

  onSearch(page: number = 0) {
    const filterLivro = new LivroFilter(page, this.quantidadeItemPagina, this.form.value.titulo);
    this.livros$ = this.livroService.findByFilter(filterLivro)
      .pipe(
        first(),
        tap(resultado => {
          this.totalElements = resultado.totalElements;
          this.totalPages = resultado.totalPages;
        }),
        catchError(falha => {
          this.errorHandlerService.handle(falha);
          return of({ livros: [], totalElements: 0, totalPages: 0 })
        })
      );
  }

  pageChanged(event: PageChangedEvent): void {
    this.page = event.page;
    this.onSearch(this.page - 1);
  }

  confirmDelete(livro: Livro) {

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

  naoTemPermissao(permissao: string) {
    return !this.auth.hasPermission(permissao);
  }

  private onRefreshPagina() {
    this.page = 1;
    this.totalElements = 0;
    this.totalPages = 0;
    this.onSearch();
  }

}
