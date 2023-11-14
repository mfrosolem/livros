package com.maira.livrosapi.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissaoModel {
	
	@Schema(example = "1")
	private Long id;

	@Schema(example = "ROLE_LIVRO_PESQUISAR")
	private String nome;
	
	@Schema(example = "Permite pesquisar livros")
	private String descricao;
}
