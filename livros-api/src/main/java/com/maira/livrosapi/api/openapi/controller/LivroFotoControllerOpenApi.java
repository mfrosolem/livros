package com.maira.livrosapi.api.openapi.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import com.maira.livrosapi.api.model.FotoLivroModel;
import com.maira.livrosapi.api.model.input.FotoLivroInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Fotos")
public interface LivroFotoControllerOpenApi {
	
	@Operation(summary = "Atualiza a foto da capa de um livro")
	FotoLivroModel atualizarFoto(@Parameter(description = "Id do livro", example = "1", required = true) Long livroId,								   
								   @RequestBody(required = true) FotoLivroInput fotoLivroInput) throws IOException;
	
	
	@Operation(summary = "Busca a foto da capa de um livro", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = FotoLivroModel.class)),
					@Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary")),
					@Content(mediaType = "image/png", schema = @Schema(type = "string", format = "binary"))
			}),
			@ApiResponse(responseCode = "400", description = "ID do livro inválido", content = {
					@Content(schema = @Schema(ref = "Problema")) }),
			@ApiResponse(responseCode = "404", description = "Foto do livro não encontrada", content = {
					@Content(schema = @Schema(ref = "Problema")) }),

	})
	FotoLivroModel buscar(
			@Parameter(description = "ID do livro", example = "1", required = true) Long livroId);
	
	
	@Operation(hidden = true)
	ResponseEntity<?> servir(Long livroId, String acceptHeader) throws HttpMediaTypeNotAcceptableException;
	
	
	@Operation(summary = "Exclui a foto da capa de um livro", responses = {
			@ApiResponse(responseCode = "204", description = "Foto do livro excluída"),
			@ApiResponse(responseCode = "400", description = "ID do livro inválido", content = {
					@Content(schema = @Schema(ref = "Problema")) }),
			@ApiResponse(responseCode = "404", description = "Foto do livro não encontrada", content = {
					@Content(schema = @Schema(ref = "Problema")) }),
	})
	ResponseEntity<Void> remover(Long livroId);

}
