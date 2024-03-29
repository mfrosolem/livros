package com.maira.livrosapi.api.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
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

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Schema(example = "1920-12-10")
	private LocalDate dataNascimento;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
