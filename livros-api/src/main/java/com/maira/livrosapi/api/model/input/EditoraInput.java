package com.maira.livrosapi.api.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditoraInput {

	@NotBlank
	private String nome;

	private String urlSiteOficial;

	private String urlFacebook;

	private String ultTwitter;

	private String urlWikipedia;

}
