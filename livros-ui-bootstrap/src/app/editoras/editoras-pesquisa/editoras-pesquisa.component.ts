import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { EMPTY, switchMap, take } from 'rxjs';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { IEditora } from './../../core/models/model';
import { ConfirmModalService } from './../../shared/confirm-modal.service';
import { ToastService } from './../../shared/toast.service';
import { EditoraFilter, EditoraService } from './../editora.service';
import { AuthService } from 'src/app/seguranca/auth.service';


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

  quantidadeItemPagina: number = 5;
  page?: number = 1;
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
    private route: ActivatedRoute,
    private auth: AuthService
  ) { }

  ngOnInit(): void {

    this.title.setTitle('Pesquisa Editoras');
    this.onSearch();

  }

  onSearch(page: number = 0) {
    const filterEditora = new EditoraFilter(page, this.quantidadeItemPagina, this.form.value.nome);
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

  pageChanged(event: PageChangedEvent): void {
    this.page = event.page;
    this.onSearch(this.page - 1);
  }

  confirmDelete(editora: IEditora) {

    const resultado$ =
      this.confirmModalService.showConfirm('Confirmação', `Deseja excluir ${editora.nome}`);

    resultado$.asObservable()
      .pipe(
        take(1),
        switchMap(resultConfirm => resultConfirm ? this.editoraService.remove(editora.id) : EMPTY)
      )
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

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  onEdit(editora: IEditora) {
    this.router.navigate(['edit', editora.id], { relativeTo: this.route });
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
