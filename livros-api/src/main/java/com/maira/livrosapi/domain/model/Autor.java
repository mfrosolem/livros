package com.maira.livrosapi.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

	private LocalDate dataNascimento;

	private LocalDate dataFalecimento;

	private String paisNascimento;

	private String estadoNascimento;

	private String cidadeNascimento;

	private String biografia;

	private String urlSiteOficial;

	private String urlFacebook;

	private String urlTwitter;
	
	private String urlWikipedia;
	
	@Builder.Default
	@OneToMany(mappedBy = "autor")
	private List<Livro> livros = new ArrayList<>();

}
