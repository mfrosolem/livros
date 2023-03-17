package com.maira.livrosapi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

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
