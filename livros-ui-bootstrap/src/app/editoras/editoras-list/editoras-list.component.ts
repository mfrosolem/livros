import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Editora } from '../../core/models/editora/editora';

@Component({
  selector: 'app-editoras-list',
  templateUrl: './editoras-list.component.html',
  styleUrls: ['./editoras-list.component.css'],
  preserveWhitespaces: true
})
export class EditorasListComponent {

  @Input() editoras: Editora[] = [];
  @Output() edit = new EventEmitter(false);
  @Output() remove = new EventEmitter(false);
  @Output() naoTemPermissao = new EventEmitter(false);

  onEdit(editora: Editora) {
    this.edit.emit(editora);
  }

  onRemove(editora: Editora) {
    this.remove.emit(editora);
  }

  onNaoTemPermissao(nomePermissao: string) {
    this.naoTemPermissao.emit(nomePermissao);
  }

}
