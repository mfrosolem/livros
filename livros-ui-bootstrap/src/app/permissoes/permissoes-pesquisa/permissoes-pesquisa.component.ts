import { Component, OnInit } from '@angular/core';
import { PermissaoFilter, PermissaoService } from '../permissao.service';
import { IPermissao } from 'src/app/core/models/model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ErrorHandlerService } from 'src/app/core/error-handler.service';
import { AuthService } from 'src/app/seguranca/auth.service';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';

@Component({
  selector: 'app-permissoes-pesquisa',
  templateUrl: './permissoes-pesquisa.component.html',
  styleUrls: ['./permissoes-pesquisa.component.css']
})
export class PermissoesPesquisaComponent implements OnInit {

  permissoes: IPermissao[] = [];

  form: FormGroup = this.formBuilder.group({
    nome: []
  });

  quantidadeItemPagina: number = 10;
  page?: number = 1;
  totalRegistros = 0;
  totalPages = 0;

  constructor(
    private permissaoService: PermissaoService,
    private formBuilder: FormBuilder,
    private title: Title,
    private errorHandlerService: ErrorHandlerService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    this.title.setTitle('Pesquisa Permissões');
    this.onSearch();
  }

  onSearch(page: number = 0) {
    const filterPermissao = new PermissaoFilter(page, this.quantidadeItemPagina, this.form.value.nome);
    this.permissaoService.findByFilter(filterPermissao).subscribe({
      next: (dados) => {
        this.permissoes = dados.permissoes;
        this.totalRegistros = dados.totalElements;
        this.totalPages = dados.totalPages;
      }, 
      error: (falha) => this.errorHandlerService.handle(falha)
    });
  }


  pageChanged(event: PageChangedEvent): void {
    this.page = event.page;
    this.onSearch(this.page - 1);
  }

  naoTemPermissao(permissao: string) {
    return !this.auth.hasPermission(permissao);
  }


}
