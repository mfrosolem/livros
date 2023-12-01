import { Component, OnInit } from '@angular/core';
import { PermissaoFilter, PermissaoService } from '../permissao.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ErrorHandlerService } from '../../core/error-handler.service';
import { AuthService } from '../../seguranca/auth.service';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { Observable, catchError, first, map, of, tap } from 'rxjs';
import { PermissaoPage } from '../../core/models/permissao/permissao-page';

@Component({
  selector: 'app-permissoes-pesquisa',
  templateUrl: './permissoes-pesquisa.component.html',
  styleUrls: ['./permissoes-pesquisa.component.css']
})
export class PermissoesPesquisaComponent implements OnInit {

  permissoes$: Observable<PermissaoPage> | null = null;

  form: FormGroup = this.formBuilder.group({
    nome: []
  });

  quantidadeItemPagina: number = 10;
  page?: number = 1;
  totalElements = 0;
  totalPages = 0;

  constructor(
    private permissaoService: PermissaoService,
    private formBuilder: FormBuilder,
    private title: Title,
    private errorHandlerService: ErrorHandlerService,
    private auth: AuthService
  ) {
    this.onSearch();
  }

  ngOnInit(): void {
    this.title.setTitle('Pesquisa Permissões');    
  }

  onSearch(page: number = 0) {
    const filterPermissao = new PermissaoFilter(page, this.quantidadeItemPagina, this.form.value.nome);
    this.permissoes$ = this.permissaoService.findByFilter(filterPermissao)
      .pipe(
        first(),
        tap(resultado => {
          this.totalElements = resultado.totalElements;
          this.totalPages = resultado.totalPages;
        }),
        catchError(falha => {
          this.errorHandlerService.handle(falha);
          return of({ permissoes: [], totalElements: 0, totalPages: 0 })
        })
      );
  }


  pageChanged(event: PageChangedEvent): void {
    this.page = event.page;
    this.onSearch(this.page - 1);
  }

  naoTemPermissao(permissao: string) {
    return !this.auth.hasPermission(permissao);
  }


}
