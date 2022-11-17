package com.maira.livrosapi.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivroModel {
	
	private Long id;
	
	private String isbn;
	
	private String titulo;
	
	private String subtitulo;
	
	private String idioma;
	
	private String serieColecao;
	
	private Long volume;
	
	private String tradutor;
	
	private EditoraModel editora;
	
	private Long ano;
	
	private Long edicao;
	
	private Long paginas;
	
	private GeneroModel genero;
	
	private String sinopse;
	
	private AutorModel autor;

}
