package com.maira.livrosapi.api.openapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.maira.livrosapi.api.model.EditoraModel;
import com.maira.livrosapi.api.model.input.EditoraInput;
import com.maira.livrosapi.core.springdoc.PageableDocParameter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Editoras")
public interface EditoraControllerOpenApi {
	
	@PageableDocParameter
	@Operation(summary = "Lista as editoras com paginação")
	public Page<EditoraModel> listar(
			@Parameter(required = false) String descricao,
			@Parameter(hidden = true) Pageable pageable);
	
	@Operation(summary = "Busca uma editora por ID",
			responses = {
					@ApiResponse(responseCode = "200"),
					@ApiResponse(responseCode = "400", description = "ID da editora inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
							),
					@ApiResponse(responseCode = "404", description = "Editora não encontrada",
					content = @Content(schema = @Schema(ref = "Problema"))
							)
	})
	public EditoraModel buscar(@Parameter(description = "ID de uma editora", example = "1", required = true) Long editoraId);
	
	
	
	@Operation(summary = "Cadastra uma editora",
			responses = {
					@ApiResponse(responseCode = "201", description = "Editora cadastrada")
			})
	public EditoraModel adicionar(@RequestBody(description = "Representação de uma editora") EditoraInput editoraInput);
	
	
	
	@Operation(summary = "Atualiza uma editora por ID",
			responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID da editora inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "404", description = "Editora não encontrada",
					content = @Content(schema = @Schema(ref = "Problema"))
			)
		})
	public EditoraModel atualizar(
			@Parameter(description = "ID de uma editora", example = "1", required = true) Long editoraId,  
			@RequestBody(description = "Representação de uma editora com dados atualizados", required = true) EditoraInput editoraInput);

	
	
	
	@Operation(summary = "Exclui uma editora por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Editora excluída"),
			@ApiResponse(responseCode = "400", description = "ID da editora inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "404", description = "Editora não encontrada",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "409", description = "Editora não pode ser excluída, está em uso",
			content = @Content(schema = @Schema(ref = "Problema"))
			)
	})
	public void remover(@Parameter(description = "ID de uma editora", example = "1", required = true) Long editoraId);

}
