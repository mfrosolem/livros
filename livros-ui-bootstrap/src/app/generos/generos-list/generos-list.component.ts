import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Genero } from '../../core/models/genero/genero';

@Component({
  selector: 'app-generos-list',
  templateUrl: './generos-list.component.html',
  styleUrls: ['./generos-list.component.css'],
  preserveWhitespaces: true
})
export class GenerosListComponent {

  @Input() generos: Genero[] = [];
  @Input() canNotEdit: boolean = true;
  @Input() canNotRemove: boolean = true;
  @Output() edit = new EventEmitter(false);
  @Output() remove = new EventEmitter(false);

  onEdit(genero: Genero) {
    this.edit.emit(genero);
  }

  onRemove(genero: Genero) {
    this.remove.emit(genero);
  }


}
