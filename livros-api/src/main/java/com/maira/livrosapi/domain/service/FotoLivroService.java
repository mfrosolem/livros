package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.model.FotoLivro;

import java.io.InputStream;

public interface FotoLivroService {

    FotoLivro salvar(FotoLivro foto, InputStream dadosArquivos);
    FotoLivro buscarOuFalhar(Long livroId);
    void excluir(Long livroId);
}
