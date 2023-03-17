import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { EMPTY, switchMap, take } from 'rxjs';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { IAutor } from './../../core/models/model';
import { ConfirmModalService } from './../../shared/confirm-modal.service';
import { ToastService } from './../../shared/toast.service';
import { AutorFilter, AutorService } from './../autor.service';


@Component({
  selector: 'app-autores-pesquisa',
  templateUrl: './autores-pesquisa.component.html',
  styleUrls: ['./autores-pesquisa.component.css'],
  preserveWhitespaces: true
})
export class AutoresPesquisaComponent implements OnInit {

  autores: IAutor[] = [];
  form: FormGroup = this.formBuilder.group({
    nome: []
  });

  quantidadeItemPagina: number = 5;
  page?: number = 1;
  totalRegistros = 0;
  totalPages = 0;


  constructor(
    private formBuilder: FormBuilder,
    private autorService: AutorService,
    private errorHandlerService: ErrorHandlerService,
    private toastService: ToastService,
    private confirmModalService: ConfirmModalService,
    private title: Title,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.title.setTitle('Pesquisa Autores');
    this.onSearch();
  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  onEdit(autor: IAutor) {
    this.router.navigate(['edit', autor.id], { relativeTo: this.route });
  }

  onSearch(page: number = 0) {
    const filterAutor = new AutorFilter(page, this.quantidadeItemPagina, this.form.value.nome);
    this.autorService.findByFilter(filterAutor)
      .subscribe({
        next: (dados) => {
          this.autores = dados.autores;
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

  confirmDelete(autor: IAutor) {

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

  private onRefreshPagina() {
    this.page = 1;
    this.totalRegistros = 0;
    this.totalPages = 0;
    this.onSearch();
  }

}
