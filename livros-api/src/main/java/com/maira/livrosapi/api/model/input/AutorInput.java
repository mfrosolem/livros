package com.maira.livrosapi.api.model.input;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AutorInput {

	@Schema(example = "Chaya")
	@NotBlank
	private String nome;
	
	@Schema(example = "Pinkhasivna Lispector")
	@NotBlank
	private String sobrenome;

	@Schema(example = "Clarice Lispector")
	private String nomeConhecido;

	@Schema(example = "F")
	private String sexo;

	@Schema(example = "1920-12-10")
	private LocalDate dataNascimento;

	@Schema(example = "1977-12-09")
	private LocalDate dataFalecimento;

	@Schema(example = "Ucrânia")
	private String paisNascimento;

	
	private String estadoNascimento;

	@Schema(example = "Chechelnyk")
	private String cidadeNascimento;

	private String biografia;

	private String urlSiteOficial;

	private String urlFacebook;

	private String urlTwitter;
	
	private String urlWikipedia;

}
