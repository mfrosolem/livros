package com.maira.livrosapi.domain.service.listener;

import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.service.EnvioEmailService;
import com.maira.livrosapi.domain.service.EnvioEmailService.Mensagem;
import com.maira.livrosapi.domain.service.event.UsuarioCadastradoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificacaoUsuarioCadastradoListener {

    private final EnvioEmailService envioEmailService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void aoCadastrarUsuario(UsuarioCadastradoEvent event) {
        Usuario usuario = event.getUsuario();
        var mensagem = Mensagem.builder()
                .assunto("Cadastro realizado")
                .corpo("emails/acesso-temporario.html")
                .variavel("usuario", usuario)
                .destinatario(usuario.getEmail())
                .build();

        envioEmailService.enviar(mensagem);
    }
}
