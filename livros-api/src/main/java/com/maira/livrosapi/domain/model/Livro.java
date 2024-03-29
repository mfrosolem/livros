package com.maira.livrosapi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Livro {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
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
