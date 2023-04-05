package com.maira.livrosapi.api.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneroInput {

	@Schema(example = "Romance")
	@NotBlank
	private String descricao;
}
