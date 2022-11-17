package com.maira.livrosapi.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class Livro {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String isbn;
	
	@Column(nullable = false)
	private String titulo;
	
	private String subtitulo;
	
	@Column(nullable = false)
	private String idioma;
	
	private String serieColecao;
	
	private Long volume;
	
	private String tradutor;
	
	@ManyToOne //eager
	@JoinColumn(name = "editora_id", nullable = false)
	private Editora editora;
	
	@Column(nullable = false)
	private Long ano;
	
	@Column(nullable = false)
	private Long edicao;
	
	@Column(nullable = false)
	private Long paginas;
	
	@ManyToOne //eager
	@JoinColumn(name = "genero_id", nullable = false)
	private Genero genero;
	
	private String sinopse;
	
	@ManyToOne //eager
	@JoinColumn(name = "autor_id", nullable = false)
	private Autor autor;

}
