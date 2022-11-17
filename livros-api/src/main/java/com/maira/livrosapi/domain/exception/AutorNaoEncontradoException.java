package com.maira.livrosapi.domain.exception;

public class AutorNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	
	public AutorNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public AutorNaoEncontradoException(Long autorId) {
		this(String.format("Não existe um cadastro de autor(a) com o código %d", autorId));
	}

	
	

}
