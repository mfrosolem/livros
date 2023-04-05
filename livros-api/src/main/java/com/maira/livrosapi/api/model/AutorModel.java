package com.maira.livrosapi.api.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutorModel {

	@Schema(example = "Romance")
	private Long id;

	@Schema(example = "Chaya")
	private String nome;
	
	@Schema(example = "Pinkhasivna Lispector")
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

	private String ultTwitter;

	private String urlWikipedia;
}
