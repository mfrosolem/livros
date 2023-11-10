package com.maira.livrosapi.api.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UsuarioInput {
	
	@Schema(example = "João da Silva")
	@NotBlank
	private String nome;
	
	@Schema(example = "joao.silva@livros.com")
	@NotBlank
	@Email
	private String email;

}
