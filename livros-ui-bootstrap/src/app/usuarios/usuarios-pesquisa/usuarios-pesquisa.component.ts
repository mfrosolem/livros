import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { EMPTY, Observable, catchError, first, of, switchMap, take, tap } from 'rxjs';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';

import { UsuarioFilter, UsuarioService } from '../usuario.service';
import { ErrorHandlerService } from '../../core/error-handler.service';
import { ConfirmModalService } from '../../shared/confirm-modal.service';
import { AuthService } from '../../seguranca/auth.service';
import { Usuario } from '../../core/models/usuario/usuario';
import { UsuarioPage } from '../../core/models/usuario/usuario-page';
import { ToastService } from '../../shared/toast.service';

@Component({
  selector: 'app-usuarios-pesquisa',
  templateUrl: './usuarios-pesquisa.component.html',
  styleUrls: ['./usuarios-pesquisa.component.css']
})
export class UsuariosPesquisaComponent implements OnInit {

  notAllowed = true;

  usuarios$: Observable<UsuarioPage> | null = null;

  form: FormGroup = this.formBuilder.group({
    nome: []
  });

  quantidadeItemPagina: number = 10;
  page?: number = 1;
  totalElements: number = 0;
  totalPages: number = 0;

  constructor(
    private usuarioService: UsuarioService,
    private title: Title,
    private formBuilder: FormBuilder,
    private errorHandlerService: ErrorHandlerService,
    private confirmModalService: ConfirmModalService,
    private toastService: ToastService,
    private router: Router,
    private route: ActivatedRoute,
    private auth: AuthService
  ) {
    this.notAllowed = this.naoTemPermissao('CADASTRAR_USUARIOS_GRUPOS_PERMISSOES');
    this.onSearch();
  }
  ngOnInit(): void {
    this.title.setTitle("Pesquisa Usuários");
  }

  onSearch(page: number = 0) {
    const filterUsuario = new UsuarioFilter(page, this.quantidadeItemPagina, this.form.value.nome);

    this.usuarios$ = this.usuarioService.findByFilter(filterUsuario)
      .pipe(
        first(),
        tap(resultado => {
          this.totalElements = resultado.totalElements;
          this.totalPages = resultado.totalPages;
        }),
        catchError(falha => {
        this.errorHandlerService.handle(falha);
        return of({ usuarios: [], totalElements: 0, totalPages: 0 })
      })
      );
  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  onEdit(usuario: Usuario) {
    this.router.navigate(['edit', usuario.id], { relativeTo: this.route });
  }

  confirmDelete(usuario: Usuario) {
    const resultado$ = 
    this.confirmModalService.showConfirm('Confirmação', `Deseja excluir ${usuario.nome}`);

    resultado$.asObservable()
    .pipe(
      take(1),
      switchMap(resultConfirm => resultConfirm ? this.usuarioService.remove(usuario.id) : EMPTY)
    )
    .subscribe({
      next: () => {
        this.toastService.showSuccessToast('Usuário excluído com sucesso!');
        this.onRefreshPagina();
      },
      error: (falha) => this.errorHandlerService.handle(falha)
    });
  }

  pageChanged(event: PageChangedEvent) {
    this.page = event.page;
    this.onSearch(this.page - 1);
  }

  naoTemPermissao(permissao: string) {
    return !this.auth.hasPermission(permissao);
  }

  private onRefreshPagina() {
    this.page = 1;
    this.onSearch();
  }

}
