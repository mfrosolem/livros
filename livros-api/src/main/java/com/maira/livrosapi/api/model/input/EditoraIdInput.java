package com.maira.livrosapi.api.model.input;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditoraIdInput {

	@NotNull
	private Long id;
}
