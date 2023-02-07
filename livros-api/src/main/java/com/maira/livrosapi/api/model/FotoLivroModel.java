package com.maira.livrosapi.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoLivroModel {

	private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;

}
