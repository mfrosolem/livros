import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { IGenero } from './../../core/models/model';
import { ConfirmModalService } from './../../shared/confirm-modal.service';
import { ToastService } from './../../shared/toast.service';
import { GeneroFilter, GeneroService } from './../genero.service';

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


  currentPage = 0;
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
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.title.setTitle('Pesquisa Gêneros');
    this.search();
  }

  search(page: number = 0) {
    const filterGenero = new GeneroFilter(page, 5, this.form.value.descricao);
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

  pageChanged(): void {
    this.search(this.currentPage - 1);
  }

  confirmDelete(genero: IGenero) {
    const resultado = this.confirmModalService.showConfirm('Confirmação', `Deseja excluir ${genero.descricao}`);
    resultado.then(res => {
      this.deleteRegister(genero.id);
    })
      .catch(erro => {
      });
  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  onEdit(genero: IGenero) {
    this.router.navigate(['edit', genero.id], { relativeTo: this.route });
  }

  private deleteRegister(id: number) {
    this.generoService.remove(id)
      .subscribe({
        next: () => {
          this.toastService.showSuccessToast('Gênero excluído com sucesso!');
          this.onRefreshPagina();
        },
        error: (falha) => this.errorHandlerService.handle(falha)
      });
  }

  private onRefreshPagina() {
    this.search();
  }


}
