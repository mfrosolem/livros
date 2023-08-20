import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { EMPTY, switchMap, take } from 'rxjs';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { IGenero } from './../../core/models/model';
import { ConfirmModalService } from './../../shared/confirm-modal.service';
import { ToastService } from './../../shared/toast.service';
import { GeneroFilter, GeneroService } from './../genero.service';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { AuthService } from 'src/app/seguranca/auth.service';

@Component({
  selector: 'app-generos-pesquisa',
  templateUrl: './generos-pesquisa.component.html',
  styleUrls: ['./generos-pesquisa.component.css'],
  preserveWhitespaces: true
})
export class GenerosPesquisaComponent implements OnInit {

  generos: IGenero[] = [];
  form: FormGroup = this.formBuilder.group({
    descricao: []
  });


  quantidadeItemPagina: number = 5;
  page?: number = 1;
  totalRegistros = 0;
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
  ) { }

  ngOnInit(): void {
    this.title.setTitle('Pesquisa Gêneros');
    this.onSearch();
  }

  onSearch(page: number = 0) {
    const filterGenero = new GeneroFilter(page, this.quantidadeItemPagina, this.form.value.descricao);
    this.generoService.findByFilter(filterGenero)
      .subscribe({
        next: (dados) => {
          this.generos = dados.generos;
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

  confirmDelete(genero: IGenero) {

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

  onEdit(genero: IGenero) {
    this.router.navigate(['edit', genero.id], { relativeTo: this.route });
  }

  naoTemPermissao(permissao: string) {
    return !this.auth.hasPermission(permissao);
  }

  private onRefreshPagina() {
    this.page = 1;
    this.totalRegistros = 0;
    this.totalPages = 0;
    this.onSearch();
  }


}
