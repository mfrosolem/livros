import { Component, OnInit } from '@angular/core';
import { IGrupo } from 'src/app/core/models/model';
import { GrupoFilter, GrupoService } from '../grupo.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ErrorHandlerService } from 'src/app/core/error-handler.service';
import { ConfirmModalService } from 'src/app/shared/confirm-modal.service';
import { ToastService } from 'src/app/shared/toast.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/seguranca/auth.service';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { EMPTY, switchMap, take } from 'rxjs';

@Component({
  selector: 'app-grupos-pesquisa',
  templateUrl: './grupos-pesquisa.component.html',
  styleUrls: ['./grupos-pesquisa.component.css']
})
export class GruposPesquisaComponent implements OnInit {

  grupos: IGrupo[] = [];

  form: FormGroup = this.formBuilder.group({
    nome: []
  });

  quantidadeItemPagina: number = 10;
  page?: number = 1;
  totalRegistros = 0;
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

  }



  ngOnInit(): void {
    this.title.setTitle("Pesquisa Grupos");
    this.onSearch();
  }

  onSearch(page: number = 0) {
    const filterGrupo = new GrupoFilter(page, this.quantidadeItemPagina, this.form.value.nome);
    this.grupoService.findByFilter(filterGrupo).subscribe({
      next: (dados) => {
          this.grupos = dados.grupos;
          this.totalRegistros = dados.totalElements;
          this.totalPages = dados.totalPages;
      },
      error: (falha) => {
        this.errorHandlerService.handle(falha);
      }
    });
  }

  pageChanged(event: PageChangedEvent): void {
    this.page = event.page;
    this.onSearch(this.page - 1);
  }

  confirmDelete(grupo: IGrupo) {
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

  onEdit(grupo: IGrupo) {
    this.router.navigate(['edit', grupo.id], { relativeTo: this.route });
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
