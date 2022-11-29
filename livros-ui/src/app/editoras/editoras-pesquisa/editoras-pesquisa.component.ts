import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

import { ErrorHandlerService } from './../../core/error-handler.service';
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

  editoras: any[] = [];
  filterEditora = new EditoraFilter();

  currentPage = 0;
  totalRegistros = 0;
  totalPages = 0;

  constructor(
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
    this.filterEditora.pagina = page;
    this.editoraService.findByFilter(this.filterEditora)
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

  confirmDelete(editora: any) {
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

  onEdit(genero: any) {
    this.router.navigate(['edit', genero.id], { relativeTo: this.route });
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
