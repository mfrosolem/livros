package com.maira.livrosapi.api.model.input;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

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

	private LocalDate dataNascimento;

	private LocalDate dataFalecimento;

	private String paisNascimento;

	private String estadoNascimento;

	private String cidadeNascimento;

	private String biografia;

	private String urlSiteOficial;

	private String urlFacebook;

	private String ultTwitter;
	
	private String urlWikipedia;

}
