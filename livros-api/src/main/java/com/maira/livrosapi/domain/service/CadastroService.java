package com.maira.livrosapi.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CadastroService<T> {

    T buscarOuFalhar(Long id);
    T salvar(T entidade);
    void excluir(Long id);
    Page<T> listByContaining(String contain, Pageable pageable);
}
