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
  @Output() edit = new EventEmitter(false);
  @Output() remove = new EventEmitter(false);
  @Output() naoTemPermissao = new EventEmitter(false);

  onEdit(livro: Livro) {
    this.edit.emit(livro);
  }

  onRemove(livro: Livro) {
    this.remove.emit(livro);
  }

  onNaoTemPermissao(nomePermissao: string) {
    this.naoTemPermissao.emit(nomePermissao);
  }

}
