import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Grupo } from '../../core/models/grupo/grupo';

@Component({
  selector: 'app-grupos-list',
  templateUrl: './grupos-list.component.html',
  styleUrls: ['./grupos-list.component.css'],
  preserveWhitespaces: true
})
export class GruposListComponent {

  @Input() grupos: Grupo[] = [];
  @Input() canNotEdit: boolean = true;
  @Input() canNotRemove: boolean = true;
  @Output() edit = new EventEmitter(false);
  @Output() remove = new EventEmitter(false);

  onEdit(grupo: Grupo) {
    this.edit.emit(grupo);
  }

  onRemove(grupo: Grupo) {
    this.remove.emit(grupo);
  }

}
