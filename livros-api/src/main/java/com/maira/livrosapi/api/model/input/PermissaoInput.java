package com.maira.livrosapi.api.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissaoInput {

    @Schema(example = "ROLE_LIVRO_PESQUISAR")
    @NotBlank
    private String nome;

    @Schema(example = "Permite pesquisar livros")
    @NotBlank
    private String descricao;
}
