package com.maira.livrosapi.api.openapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.maira.livrosapi.api.model.GeneroModel;
import com.maira.livrosapi.api.model.input.GeneroInput;
import com.maira.livrosapi.core.springdoc.PageableDocParameter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Generos")
public interface GeneroControllerOpenApi {
	
	@PageableDocParameter
	@Operation(summary = "Lista os gêneros com paginação")
	public Page<GeneroModel> listar(
			@Parameter(required = false) String descricao,
			@Parameter(hidden = true) Pageable pageable);
	
	@Operation(summary = "Busca um gênero por ID",
			responses = {
					@ApiResponse(responseCode = "200"),
					@ApiResponse(responseCode = "400", description = "ID do gênero inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
							),
					@ApiResponse(responseCode = "404", description = "Gênero não encontrado",
					content = @Content(schema = @Schema(ref = "Problema"))
							)
	})
	public GeneroModel buscar(@Parameter(description = "ID de um gênero", example = "1", required = true) Long generoId);
	
	
	@Operation(summary = "Cadastra um gênero",
			responses = {
					@ApiResponse(responseCode = "201", description = "Gênero cadastrado")
			})
	public GeneroModel adicionar(@RequestBody(description = "Representação de um gênero") GeneroInput generoInput);
	
	
	
	@Operation(summary = "Atualiza um gênero por ID",
			responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID do gênero inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "404", description = "Gênero não encontrado",
					content = @Content(schema = @Schema(ref = "Problema"))
			)
		})
	public GeneroModel atualizar(
			@Parameter(description = "ID de um gênero", example = "1", required = true) Long generoId,  
			@RequestBody(description = "Representação de um gênero com dados atualizados", required = true) GeneroInput generoInput);

	
	@Operation(summary = "Exclui um gênero por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Gênero excluído"),
			@ApiResponse(responseCode = "400", description = "ID do gênero inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "404", description = "Gênero não encontrado",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "409", description = "Gênero não pode ser excluído, está em uso",
			content = @Content(schema = @Schema(ref = "Problema"))
			)
	})
	public void remover(@Parameter(description = "ID de um gênero", example = "1", required = true) Long generoId);
}
