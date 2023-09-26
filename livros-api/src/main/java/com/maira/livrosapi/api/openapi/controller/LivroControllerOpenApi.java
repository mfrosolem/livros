package com.maira.livrosapi.api.openapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.maira.livrosapi.api.model.LivroModel;
import com.maira.livrosapi.api.model.input.LivroInput;
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
@Tag(name = "Livros")
public interface LivroControllerOpenApi {
	
	
	@PageableDocParameter
	@Operation(summary = "Lista os livros com paginação")
	public Page<LivroModel> listar(
			@Parameter(required = false) String descricao,
			@Parameter(hidden = true) Pageable pageable);
	
	
	
	@Operation(summary = "Busca um livro por ID",
			responses = {
					@ApiResponse(responseCode = "200"),
					@ApiResponse(responseCode = "400", description = "ID do livro inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
							),
					@ApiResponse(responseCode = "404", description = "Livro não encontrado",
					content = @Content(schema = @Schema(ref = "Problema"))
							)
	})
	public LivroModel buscar(@Parameter(description = "ID de um livro", example = "1", required = true) Long livroId);
	
	
	
	@Operation(summary = "Cadastra um livro",
			responses = {
					@ApiResponse(responseCode = "201", description = "Livro cadastrado")
			})
	public LivroModel adicionar(@RequestBody(description = "Representação de um livro") LivroInput livroInput);
	
	
	
	@Operation(summary = "Atualiza um livro por ID",
			responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID do livro inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "404", description = "Livro não encontrado",
					content = @Content(schema = @Schema(ref = "Problema"))
			)
		})
	public LivroModel atualizar(
			@Parameter(description = "ID de um livro", example = "1", required = true) Long livroId,  
			@RequestBody(description = "Representação de um livro com dados atualizados", required = true) LivroInput livroInput);

	
	
	@Operation(summary = "Exclui um livro por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Livro excluído"),
			@ApiResponse(responseCode = "400", description = "ID do livro inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "404", description = "Livro não encontrado",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "409", description = "Livro não pode ser excluído, está em uso",
			content = @Content(schema = @Schema(ref = "Problema"))
			)
	})
	public void remover(@Parameter(description = "ID de um livro", example = "1", required = true) Long livroId);

}
