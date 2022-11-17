package com.maira.livrosapi.domain.exception;

public class GeneroNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public GeneroNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

	public GeneroNaoEncontradoException(Long generoId) {
		this(String.format("Não existe um cadastro de genero com o código %d", generoId));
	}
}
