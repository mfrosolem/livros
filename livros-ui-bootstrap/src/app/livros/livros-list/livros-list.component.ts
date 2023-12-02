import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Livro } from '../../core/models/livro/livro';

@Component({
  selector: 'app-livros-list',
  templateUrl: './livros-list.component.html',
  styleUrls: ['./livros-list.component.css'],
  preserveWhitespaces: true
})
export class LivrosListComponent {

  @Input() livros: Livro[] = [];
  @Input() canNotEdit: boolean = true;
  @Input() canNotRemove: boolean = true;
  @Output() edit = new EventEmitter(false);
  @Output() remove = new EventEmitter(false);

  onEdit(livro: Livro) {
    this.edit.emit(livro);
  }

  onRemove(livro: Livro) {
    this.remove.emit(livro);
  }

}
