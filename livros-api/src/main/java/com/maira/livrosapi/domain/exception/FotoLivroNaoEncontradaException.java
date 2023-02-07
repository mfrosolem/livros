package com.maira.livrosapi.domain.exception;

public class FotoLivroNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public FotoLivroNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public FotoLivroNaoEncontradaException(Long livroId) {
		this(String.format("Não existe um cadastro de foto para o livro com código %d", livroId));
	}

}
