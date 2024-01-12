import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { Permissao } from '../../core/models/permissao/permissao';
import { GrupoService } from '../grupo.service';
import { PermissaoService } from '../../permissoes/permissao.service';
import { Title } from '@angular/platform-browser';
import { Location } from '@angular/common';


@Component({
  selector: 'app-grupo-permissoes',
  templateUrl: './grupo-permissoes.component.html',
  styleUrls: ['./grupo-permissoes.component.css']
})
export class GrupoPermissoesComponent {

  @Input() idGrupo: number|null = null;
  @Input() nomeGrupo: string = "";
  @Input() permissoes : Permissao[] = [];
  @Input() grupoPermissoes : Permissao[] = [];
  @Output() attach = new EventEmitter(false);
  @Output() detach = new EventEmitter(false);


  // permissoesGrupo$: Observable<Permissao[]|any> | null = null;
  // permissoes$: Observable<Permissao[]|any> | null = null;

  onAttach(permissao: Permissao) {
    this.attach.emit(permissao);
  }

  onDetach(permissao: Permissao) {
    this.detach.emit(permissao);
  }



}
