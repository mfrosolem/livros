import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { IEditora } from './../../core/models/model';
import { ConfirmModalService } from './../../shared/confirm-modal.service';
import { ToastService } from './../../shared/toast.service';
import { EditoraFilter, EditoraService } from './../editora.service';


@Component({
  selector: 'app-editoras-pesquisa',
  templateUrl: './editoras-pesquisa.component.html',
  styleUrls: ['./editoras-pesquisa.component.css'],
  preserveWhitespaces: true
})
export class EditorasPesquisaComponent implements OnInit {

  editoras: IEditora[] = [];
  form: FormGroup = this.formBuilder.group({
    nome: []
  });

  currentPage = 0;
  totalRegistros = 0;
  totalPages = 0;

  constructor(
    private formBuilder: FormBuilder,
    private editoraService: EditoraService,
    private errorHandlerService: ErrorHandlerService,
    private confirmModalService: ConfirmModalService,
    private toastService: ToastService,
    private title: Title,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {

    this.title.setTitle('Pesquisa Editoras');
    this.onSearch();

  }

  onSearch(page: number = 0) {
    const filterEditora = new EditoraFilter(page, 5, this.form.value.nome);
    this.editoraService.findByFilter(filterEditora)
      .subscribe({
        next: (dados) => {
          this.editoras = dados.editoras;
          this.totalRegistros = dados.totalElements;
          this.totalPages = dados.totalPages;
        },
        error: (falha) => {
          this.errorHandlerService.handle(falha);
        },
      });
  }

  pageChanged(): void {
    this.onSearch(this.currentPage - 1);
  }

  confirmDelete(editora: IEditora) {
    const resultado = this.confirmModalService.showConfirm('Confirmação', `Deseja excluir ${editora.nome}`);
    resultado.then(res => {
      this.deleteRegister(editora.id);
    })
      .catch(erro => {
      });
  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  onEdit(editora: IEditora) {
    this.router.navigate(['edit', editora.id], { relativeTo: this.route });
  }

  private deleteRegister(id: number) {
    this.editoraService.remove(id)
      .subscribe({
        next: () => {
          this.toastService.showSuccessToast('Editora excluída com sucesso');
          this.onRefreshPagina();
        },
        error: (erro) => {
          this.errorHandlerService.handle(erro);
        }
      });

  }

  private onRefreshPagina() {
    this.onSearch();
  }



}
