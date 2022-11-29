package com.maira.livrosapi.api.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutorModel {

	private Long id;

	private String nome;
	
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
