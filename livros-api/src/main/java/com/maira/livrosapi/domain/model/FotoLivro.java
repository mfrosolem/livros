package com.maira.livrosapi.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class FotoLivro {

	@EqualsAndHashCode.Include
	@Id
	@Column(name = "livro_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Livro livro;

	private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;
}
