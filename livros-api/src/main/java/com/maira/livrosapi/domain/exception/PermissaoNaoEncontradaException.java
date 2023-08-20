package com.maira.livrosapi.domain.exception;

public class PermissaoNaoEncontradaException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;

	public PermissaoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public PermissaoNaoEncontradaException(Long permissaoId) {
		this(String.format("Não existe cadastro de permissão com o código %d", permissaoId));
	}

}
