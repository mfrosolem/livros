package com.maira.livrosapi.api.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SenhaInput {
	
	@Schema(example = "123")
	@NotBlank
	private String senhaAtual;
	
	@Schema(example = "321")
	@NotBlank
	private String novaSenha;

}
