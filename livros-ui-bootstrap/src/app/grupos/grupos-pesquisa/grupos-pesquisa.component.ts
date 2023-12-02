import { Component, OnInit } from '@angular/core';
import { GrupoFilter, GrupoService } from '../grupo.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ErrorHandlerService } from '../../core/error-handler.service';
import { ConfirmModalService } from '../../shared/confirm-modal.service';
import { ToastService } from '../../shared/toast.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../seguranca/auth.service';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { EMPTY, Observable, catchError, first, of, switchMap, take, tap } from 'rxjs';
import { Grupo } from '../../core/models/grupo/grupo';
import { GrupoPage } from '../../core/models/grupo/grupo-page';

@Component({
  selector: 'app-grupos-pesquisa',
  templateUrl: './grupos-pesquisa.component.html',
  styleUrls: ['./grupos-pesquisa.component.css']
})
export class GruposPesquisaComponent implements OnInit {

  notAllowed = true;

  grupos$: Observable<GrupoPage> | null = null;

  form: FormGroup = this.formBuilder.group({
    nome: []
  });

  quantidadeItemPagina: number = 10;
  page?: number = 1;
  totalElements = 0;
  totalPages = 0;
  

  constructor(
    private grupoService: GrupoService,
    private formBuilder: FormBuilder,
    private title: Title,
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
    this.title.setTitle("Pesquisa Grupos");    
  }

  onSearch(page: number = 0) {
    const filterGrupo = new GrupoFilter(page, this.quantidadeItemPagina, this.form.value.nome);
    this.grupos$ = this.grupoService.findByFilter(filterGrupo)
    .pipe(
      first(),
      tap(resultado => {
        this.totalElements = resultado.totalElements;
        this.totalPages = resultado.totalPages;
      }),
      catchError(falha => {
        this.errorHandlerService.handle(falha);
        return of({ grupos: [], totalElements: 0, totalPages: 0 })
      })
    );
  }

  pageChanged(event: PageChangedEvent): void {
    this.page = event.page;
    this.onSearch(this.page - 1);
  }

  confirmDelete(grupo: Grupo) {
    const resultado$ = 
    this.confirmModalService.showConfirm('Confirmação', `Deseja excluir ${grupo.nome}`);

    resultado$.asObservable()
    .pipe(
      take(1),
      switchMap(resultConfirm => resultConfirm ? this.grupoService.remove(grupo.id) : EMPTY)
    )
    .subscribe({
      next: () => {
        this.toastService.showSuccessToast('Grupo excluído com sucesso!');
        this.onRefreshPagina();
      },
      error: (falha) => this.errorHandlerService.handle(falha)
    });
  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  onEdit(grupo: Grupo) {
    this.router.navigate(['edit', grupo.id], { relativeTo: this.route });
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
