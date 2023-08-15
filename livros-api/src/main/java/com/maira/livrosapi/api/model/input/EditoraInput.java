package com.maira.livrosapi.api.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditoraInput {

	@Schema(example = "Rocco")
	@NotBlank
	private String nome;

	private String urlSiteOficial;

	private String urlFacebook;

	private String urlTwitter;

	private String urlWikipedia;

}
