package com.maira.livrosapi.api.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
	
	@Schema(example = "1Ab$56")
	@NotBlank
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,8}$")
	private String novaSenha;

}
