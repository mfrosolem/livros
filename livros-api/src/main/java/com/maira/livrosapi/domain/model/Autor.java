package com.maira.livrosapi.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Autor {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String sobrenome;

	private String nomeConhecido;

	private String sexo;

	@Column(columnDefinition = "datetime")
	private LocalDate dataNascimento;

	@Column(columnDefinition = "datetime")
	private LocalDate dataFalecimento;

	private String paisNascimento;

	private String estadoNascimento;

	private String cidadeNascimento;

	private String biografia;

	private String urlSiteOficial;

	private String urlFacebook;

	private String ultTwitter;
	
	private String urlWikipedia;
	
	@OneToMany(mappedBy = "autor")
	private List<Livro> livros = new ArrayList<>();

}
