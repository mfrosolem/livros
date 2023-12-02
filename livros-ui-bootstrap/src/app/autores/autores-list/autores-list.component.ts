import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Autor } from '../../core/models/autor/autor';

@Component({
  selector: 'app-autores-list',
  templateUrl: './autores-list.component.html',
  styleUrls: ['./autores-list.component.css'],
  preserveWhitespaces: true
})
export class AutoresListComponent {

  @Input() autores: Autor[] = [];
  @Input() canNotEdit: boolean = true;
  @Input() canNotRemove: boolean = true;
  @Output() edit = new EventEmitter(false);
  @Output() remove = new EventEmitter(false);

  onEdit(autor: Autor) {
    this.edit.emit(autor);
  }

  onRemove(autor: Autor) {
    this.remove.emit(autor);
  }

}
