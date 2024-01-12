package com.maira.livrosapi.domain.service.event;

import com.maira.livrosapi.domain.model.Usuario;
import org.springframework.context.ApplicationEvent;


public class UsuarioCadastradoEvent extends ApplicationEvent {

    private final Usuario usuario;

    public UsuarioCadastradoEvent(Object source, Usuario usuario) {
        super(source);
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

}
