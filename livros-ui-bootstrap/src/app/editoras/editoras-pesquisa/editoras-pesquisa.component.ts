import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { EMPTY, Observable, catchError, first, of, switchMap, take, tap } from 'rxjs';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { ConfirmModalService } from './../../shared/confirm-modal.service';
import { ToastService } from './../../shared/toast.service';
import { EditoraFilter, EditoraService } from './../editora.service';
import { AuthService } from 'src/app/seguranca/auth.service';
import { Editora } from '../../core/models/editora/editora';
import { EditoraPage } from '../../core/models/editora/editora-page';


@Component({
  selector: 'app-editoras-pesquisa',
  templateUrl: './editoras-pesquisa.component.html',
  styleUrls: ['./editoras-pesquisa.component.css'],
  preserveWhitespaces: true
})
export class EditorasPesquisaComponent implements OnInit {

  editoras$: Observable<EditoraPage> | null = null;

  form: FormGroup = this.formBuilder.group({
    nome: []
  });

  quantidadeItemPagina: number = 10;
  page?: number = 1;
  totalElements = 0;
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
  ) { 
    this.onSearch();
  }

  ngOnInit(): void {
    this.title.setTitle('Pesquisa Editoras');
    
  }

  onSearch(page: number = 0) {
    const filterEditora = new EditoraFilter(page, this.quantidadeItemPagina, this.form.value.nome);

    this.editoras$ = this.editoraService.findByFilter(filterEditora)
      .pipe(
        first(),
        tap(resultado => {
          this.totalElements = resultado.totalElements;
          this.totalPages = resultado.totalPages;
        }),
        catchError(falha => {
          this.errorHandlerService.handle(falha);
          return of({ editoras: [], totalElements: 0, totalPages: 0 })
        })
      );

  }

  pageChanged(event: PageChangedEvent): void {
    this.page = event.page;
    this.onSearch(this.page - 1);
  }

  confirmDelete(editora: Editora) {

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

  onEdit(editora: Editora) {
    this.router.navigate(['edit', editora.id], { relativeTo: this.route });
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
