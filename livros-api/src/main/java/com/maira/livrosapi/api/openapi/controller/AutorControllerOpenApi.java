package com.maira.livrosapi.api.openapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.maira.livrosapi.api.model.AutorModel;
import com.maira.livrosapi.api.model.input.AutorInput;
import com.maira.livrosapi.core.springdoc.PageableDocParameter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Autores")
public interface AutorControllerOpenApi {
	
	@PageableDocParameter
	@Operation(summary = "Lista os autores com paginação")
	public Page<AutorModel> listar(
			@Parameter(required = false) String descricao,
			@Parameter(hidden = true) Pageable pageable);
	
	@Operation(summary = "Busca um autor por ID",
			responses = {
					@ApiResponse(responseCode = "200"),
					@ApiResponse(responseCode = "400", description = "ID do autor inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
							),
					@ApiResponse(responseCode = "404", description = "Autor não encontrado",
					content = @Content(schema = @Schema(ref = "Problema"))
							)
	}
			)
	public AutorModel buscar(@Parameter(description = "ID de um autor", example = "1", required = true) Long autorId);
	
	
	
	@Operation(summary = "Cadastra um autor",
			responses = {
					@ApiResponse(responseCode = "201", description = "Autor cadastrado")
			}
			)
	public AutorModel adicionar(@RequestBody(description = "Representação de um autor") AutorInput autorInput);
	
	
	
	@Operation(summary = "Atualiza um autor por ID",
			responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID do autor inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "404", description = "Autor não encontrado",
					content = @Content(schema = @Schema(ref = "Problema"))
			)
		})
	public AutorModel atualizar(
			@Parameter(description = "ID de um autor", example = "1", required = true) Long autorId,  
			@RequestBody(description = "Representação de um autor com dados atualizados", required = true) AutorInput autorInput);

	
	@Operation(summary = "Exclui um autor por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Autor excluído"),
			@ApiResponse(responseCode = "400", description = "ID do autor inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "404", description = "Autor não encontrado",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "409", description = "Autor não pode ser excluído, está em uso",
			content = @Content(schema = @Schema(ref = "Problema"))
			)
	})
	public void remover(@Parameter(description = "ID de um autor", example = "1", required = true) Long autorId);

}
