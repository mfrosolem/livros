package com.maira.livrosapi.api.model.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivroInput {
	
	@NotBlank
	private String isbn;
	
	@NotBlank
	private String titulo;
	
	private String subtitulo;
	
	@NotBlank
	private String idioma;
	
	private String serieColecao;
	
	private Long volume;
	
	private String tradutor;
	
	@Valid
	@NotNull
	private EditoraIdInput editora;
	
	@Positive
	private Long ano;
	
	@Positive
	private Long edicao;
	
	@Positive
	private Long paginas;
	
	@Valid
	@NotNull
	private GeneroIdInput genero;
	
	private String sinopse;
	
	@Valid
	@NotNull
	private AutorIdInput autor;

}
