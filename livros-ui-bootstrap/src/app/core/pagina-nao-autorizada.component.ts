import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-pagina-nao-autorizada',
  template: `
    <div class="container">
      <h1 class="text-center">Acesso negado!</h1>
    </div>
  `,
  styles: [
  ]
})
export class PaginaNaoAutorizadaComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
