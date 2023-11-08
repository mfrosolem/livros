package com.maira.livrosapi.api.openapi.controller;

import com.maira.livrosapi.api.model.PermissaoModel;
import com.maira.livrosapi.api.model.input.PermissaoInput;
import com.maira.livrosapi.core.springdoc.PageableDocParameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Permissões")
public interface PermissaoControllerOpenApi {


	@PageableDocParameter
	@Operation(summary = "Lista as permissões com paginação")
	public Page<PermissaoModel> listar(
			@Parameter(required = false) String nome,
			@Parameter(hidden = true) Pageable pageable);

	@Operation(summary = "Busca uma permissão por ID",
			responses = {
					@ApiResponse(responseCode = "200"),
					@ApiResponse(responseCode = "400", description = "ID da permissão inválida",
							content = @Content(schema = @Schema(ref = "Problema"))
					),
					@ApiResponse(responseCode = "404", description = "Permissão não encontrada",
							content = @Content(schema = @Schema(ref = "Problema"))
					)
			})
	public PermissaoModel buscar(@Parameter(description = "ID de uma permissão", example = "1", required = true) Long permissaoId);


	@Operation(summary = "Cadastra uma permissão",
			responses = {
					@ApiResponse(responseCode = "201", description = "Permissão cadastrada")
			})
	public PermissaoModel adicionar(@RequestBody(description = "Representação de uma permissão") PermissaoInput permissaoInput);



	@Operation(summary = "Atualiza uma permissão por ID",
			responses = {
					@ApiResponse(responseCode = "200"),
					@ApiResponse(responseCode = "400", description = "ID da permissão inválida",
							content = @Content(schema = @Schema(ref = "Problema"))
					),
					@ApiResponse(responseCode = "404", description = "Permissão não encontrada",
							content = @Content(schema = @Schema(ref = "Problema"))
					)
			})
	public PermissaoModel atualizar(
			@Parameter(description = "ID de uma permissão", example = "1", required = true) Long permissaoId,
			@RequestBody(description = "Representação de uma permissão com dados atualizados", required = true) PermissaoInput permissaoInput);


	@Operation(summary = "Exclui uma permissão por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Permissão excluída"),
			@ApiResponse(responseCode = "400", description = "ID da permissão inválida",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "404", description = "Permissão não encontrada",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "409", description = "Permissão não pode ser excluída, está em uso",
					content = @Content(schema = @Schema(ref = "Problema"))
			)
	})
	public void remover(@Parameter(description = "ID de uma permissão", example = "1", required = true) Long permissaoId);

}
