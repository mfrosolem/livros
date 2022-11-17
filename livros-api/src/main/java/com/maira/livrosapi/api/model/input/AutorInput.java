package com.maira.livrosapi.api.model.input;

import java.time.OffsetDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutorInput {

	@NotBlank
	private String nome;
	
	@NotBlank
	private String sobrenome;

	private String nomeConhecido;

	private String sexo;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime dataNascimento;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime dataFalecimento;

	private String paisNascimento;

	private String estadoNascimento;

	private String cidadeNascimento;

	private String biografia;

	private String urlSiteOficial;

	private String urlFacebook;

	private String ultTwitter;
	
	private String urlWikipedia;

}
