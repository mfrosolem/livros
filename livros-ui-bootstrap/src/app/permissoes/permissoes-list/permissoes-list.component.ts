import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Permissao } from '../../core/models/permissao/permissao';

@Component({
  selector: 'app-permissoes-list',
  templateUrl: './permissoes-list.component.html',
  styleUrls: ['./permissoes-list.component.css'],
  preserveWhitespaces: true
})
export class PermissoesListComponent {

  @Input() permissoes: Permissao[] = [];

}
