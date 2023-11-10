package com.maira.livrosapi.api.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LivroInput {
	
	@Schema(example = "9788532508126")
	@NotBlank
	private String isbn;
	
	@Schema(example = "A Hora da Estrela")
	@NotBlank
	private String titulo;
	
	private String subtitulo;
	
	@Schema(example = "Português")
	@NotBlank
	private String idioma;
	
	private String serieColecao;
	
	private Long volume;
	
	private String tradutor;
	
	@Valid
	@NotNull
	private EditoraIdInput editora;
	
	@Schema(example = "1998")
	@Positive
	private Long ano;
	
	@Schema(example = "1")
	@Positive
	private Long edicao;
	
	@Schema(example = "87")
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
