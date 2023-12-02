import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { EMPTY, Observable, catchError, first, map, of, switchMap, take, tap } from 'rxjs';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { ConfirmModalService } from './../../shared/confirm-modal.service';
import { ToastService } from './../../shared/toast.service';
import { AutorFilter, AutorService } from './../autor.service';
import { AuthService } from '../../seguranca/auth.service';
import { Autor } from '../../core/models/autor/autor';
import { AutorPage } from '../../core/models/autor/autor-pages';


@Component({
  selector: 'app-autores-pesquisa',
  templateUrl: './autores-pesquisa.component.html',
  styleUrls: ['./autores-pesquisa.component.css'],
  preserveWhitespaces: true
})
export class AutoresPesquisaComponent implements OnInit {

  naoPodeRemover = true;
  naoPodeEditar = true;

  autores$: Observable<AutorPage> | null = null;
  form: FormGroup = this.formBuilder.group({
    nome: []
  });

  quantidadeItemPagina: number = 10;
  page?: number = 1;
  totalElements = 0;
  totalPages = 0;


  constructor(
    private formBuilder: FormBuilder,
    private autorService: AutorService,
    private errorHandlerService: ErrorHandlerService,
    private toastService: ToastService,
    private confirmModalService: ConfirmModalService,
    private title: Title,
    private router: Router,
    private route: ActivatedRoute,
    private auth: AuthService
  ) {
    this.naoPodeRemover = this.naoTemPermissao('REMOVER_AUTOR');
    this.naoPodeEditar = this.naoTemPermissao('CADASTRAR_AUTOR');

    this.onSearch();
   }

  ngOnInit(): void {
    this.title.setTitle('Pesquisa Autores');
    
  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  onEdit(autor: Autor) {
    this.router.navigate(['edit', autor.id], { relativeTo: this.route });
  }

  onSearch(page: number = 0) {
    const filterAutor = new AutorFilter(page, this.quantidadeItemPagina, this.form.value.nome);
    this.autores$ = this.autorService.findByFilter(filterAutor)
      .pipe(
        first(),
        tap(resultado => {
          this.totalElements = resultado.totalElements;
          this.totalPages = resultado.totalPages;
        }),
        catchError(falha => {
          this.errorHandlerService.handle(falha);
          return of({ autores: [], totalElements: 0, totalPages: 0 })
        })
      );
  }

  pageChanged(event: PageChangedEvent): void {
    this.page = event.page;
    this.onSearch(this.page - 1);
  }

  confirmDelete(autor: Autor) {

    const resultado$ =
      this.confirmModalService.showConfirm('Confirmação', `Deseja excluir ${autor.nome} ${autor.sobrenome}`);

    resultado$.asObservable()
      .pipe(
        take(1),
        switchMap(resultConfirm => resultConfirm ? this.autorService.remove(autor.id) : EMPTY)
      )
      .subscribe({
          next: () => {
            this.toastService.showSuccessToast('Autor excluído com sucesso');
            this.onRefreshPagina();
          },
          error: (erro) => {
            this.errorHandlerService.handle(erro);
          }
        });
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
