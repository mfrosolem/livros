package com.maira.livrosapi.api.model.input;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneroInput {

	@NotBlank
	private String descricao;
}
