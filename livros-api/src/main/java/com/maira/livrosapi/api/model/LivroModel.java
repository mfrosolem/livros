package com.maira.livrosapi.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LivroModel {
	
	@Schema(example = "1")
	private Long id;
	
	@Schema(example = "9788532508126")
	private String isbn;
	
	@Schema(example = "A Hora da Estrela")
	private String titulo;
	
	private String subtitulo;
	
	@Schema(example = "Português")
	private String idioma;
	
	private String serieColecao;
	
	private Long volume;
	
	private String tradutor;
	
	private EditoraModel editora;
	
	@Schema(example = "1998")
	private Long ano;
	
	@Schema(example = "1")
	private Long edicao;
	
	@Schema(example = "87")
	private Long paginas;
	
	private GeneroModel genero;
	
	private String sinopse;
	
	private AutorModel autor;

}
