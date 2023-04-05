package com.maira.livrosapi.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditoraModel {

	@Schema(example = "1")
	private Long id;

	@Schema(example = "Rocco")
	private String nome;

	private String urlSiteOficial;

	private String urlFacebook;

	private String ultTwitter;

	private String urlWikipedia;
}
