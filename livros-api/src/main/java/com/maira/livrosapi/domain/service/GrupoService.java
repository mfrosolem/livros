package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.model.Grupo;

public interface GrupoService extends CadastroService<Grupo>{

    void associarPermissao(Long grupoId, Long permissaoId);

    void desassociarPermissao(Long grupoId, Long permissaoId);
}
