package com.maira.livrosapi.domain.exception;

public class EditoraNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public EditoraNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public EditoraNaoEncontradaException(Long editoraId) {
		this(String.format("Não existe um cadastro de editora com o código %d", editoraId));
	}

}
