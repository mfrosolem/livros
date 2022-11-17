package com.maira.livrosapi.api.model.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
