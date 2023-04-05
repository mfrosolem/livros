package com.maira.livrosapi.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneroModel {

	@Schema(example = "1")
	private Long id;
	
	@Schema(example = "Romance")
	private String descricao;
}
