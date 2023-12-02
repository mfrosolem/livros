import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { EMPTY, Observable, catchError, first, of, switchMap, take, tap } from 'rxjs';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { ConfirmModalService } from './../../shared/confirm-modal.service';
import { ToastService } from './../../shared/toast.service';
import { GeneroFilter, GeneroService } from './../genero.service';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { AuthService } from 'src/app/seguranca/auth.service';
import { Genero } from '../../core/models/genero/genero';
import { GeneroPage } from '../../core/models/genero/genero-page';

@Component({
  selector: 'app-generos-pesquisa',
  templateUrl: './generos-pesquisa.component.html',
  styleUrls: ['./generos-pesquisa.component.css'],
  preserveWhitespaces: true
})
export class GenerosPesquisaComponent implements OnInit {

  naoPodeRemover = true;
  naoPodeEditar = true;

  generos$: Observable<GeneroPage> | null = null;

  form: FormGroup = this.formBuilder.group({
    descricao: []
  });


  quantidadeItemPagina: number = 10;
  page?: number = 1;
  totalElements = 0;
  totalPages = 0;

  constructor(
    private formBuilder: FormBuilder,
    private generoService: GeneroService,
    private errorHandlerService: ErrorHandlerService,
    private confirmModalService: ConfirmModalService,
    private toastService: ToastService,
    private title: Title,
    private router: Router,
    private route: ActivatedRoute,
    private auth: AuthService
  ) { 
    this.naoPodeRemover = this.naoTemPermissao('REMOVER_GENERO');
    this.naoPodeEditar = this.naoTemPermissao('CADASTRAR_GENERO');
    this.onSearch();
  }

  ngOnInit(): void {
    this.title.setTitle('Pesquisa Gêneros');
  }

  onSearch(page: number = 0) {
    const filterGenero = new GeneroFilter(page, this.quantidadeItemPagina, this.form.value.descricao);

    this.generos$ = this.generoService.findByFilter(filterGenero)
      .pipe(
        first(),
        tap(resultado => {
          this.totalElements = resultado.totalElements;
          this.totalPages = resultado.totalPages;
        }),
        catchError(falha => {
          this.errorHandlerService.handle(falha);
          return of({ generos: [], totalElements: 0, totalPages: 0 })
        })
      );
  }

  pageChanged(event: PageChangedEvent): void {
    this.page = event.page;
    this.onSearch(this.page - 1);
  }

  confirmDelete(genero: Genero) {

    const resultado$ =
      this.confirmModalService.showConfirm('Confirmação', `Deseja excluir ${genero.descricao}`);

    resultado$.asObservable()
      .pipe(
        take(1),
        switchMap(resultConfirm => resultConfirm ? this.generoService.remove(genero.id) : EMPTY)
      )
      .subscribe({
        next: () => {
          this.toastService.showSuccessToast('Gênero excluído com sucesso!');
          this.onRefreshPagina();
        },
        error: (falha) => this.errorHandlerService.handle(falha)
      });
  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  onEdit(genero: Genero) {
    this.router.navigate(['edit', genero.id], { relativeTo: this.route });
  }

  naoTemPermissao(permissao: string) {
    return !this.auth.hasPermission(permissao);
  }

  private onRefreshPagina() {
    this.page = 1;
    this.onSearch();
    this.totalElements = 0;
    this.totalPages = 0;
    
  }


}
