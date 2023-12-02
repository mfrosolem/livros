package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.model.Usuario;

public interface UsuarioService extends CadastroService<Usuario>{

    void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha);

}
