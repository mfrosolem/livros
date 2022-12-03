import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { ToastService } from './../shared/toast.service';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor(
    private toastService: ToastService
  ) { }

  handle(errorResponse: any) {
    let mensagem: string;
    let listMessage: string[] = [];

    if (typeof errorResponse === 'string') {
      mensagem = errorResponse;
    } else if (errorResponse instanceof HttpErrorResponse
      && errorResponse.status >= 400 && errorResponse.status <= 499) {
      mensagem = 'Ocorreu erro ao processar a sua solicitação.';

      if (errorResponse.status === 403) {
        mensagem = 'Você não tem permissão para executar esta ação.';
      }

      if (errorResponse.status === 409) {
        mensagem = errorResponse.error.userMessage;
      }

      try {
        // mensagem = errorResponse.error.objects[0].userMessage;

        if (errorResponse.error.objects) {

          errorResponse.error.objects.forEach((valor: any) => {
            listMessage.push(valor.userMessage);
          });

        } else {
          mensagem = errorResponse.error.userMessage;
        }

      } catch (e) { }

      console.error('Ocorreu um erro', errorResponse);
    } else {
      mensagem = 'Erro ao processar serviço remoto. Tente novamente.';
    }

    if (listMessage.length === 0) {
      listMessage.push(mensagem);
    }

    listMessage.forEach((simpleMessage: string) => {
      this.toastService.showErrorToast(simpleMessage);
    })

  }


}
