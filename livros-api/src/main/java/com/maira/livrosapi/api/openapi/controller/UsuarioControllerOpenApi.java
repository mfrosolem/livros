package com.maira.livrosapi.api.openapi.controller;

import com.maira.livrosapi.api.model.UsuarioModel;
import com.maira.livrosapi.api.model.input.SenhaInput;
import com.maira.livrosapi.api.model.input.UsuarioInput;
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
import org.springframework.http.ResponseEntity;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Usuários")
public interface UsuarioControllerOpenApi {
	
	
	@PageableDocParameter
	@Operation(summary = "Lista os usuários com paginação")
	public Page<UsuarioModel> listar(
			@Parameter(required = false) String nome,
			@Parameter(hidden = true) Pageable pageable);
	
	
	@Operation(summary = "Busca um usuário por ID",
			responses = {
					@ApiResponse(responseCode = "200"),
					@ApiResponse(responseCode = "400", description = "ID do usuário inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
							),
					@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
					content = @Content(schema = @Schema(ref = "Problema"))
							)
	}
			)
	public UsuarioModel buscar(@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId);
	
	
	@Operation(summary = "Cadastra um usuário",
			responses = {
					@ApiResponse(responseCode = "201", description = "Usuário cadastrado")
			}
			)
	public UsuarioModel adicionar(@RequestBody(description = "Representação de um usuário") UsuarioInput usuarioInput);
	
	@Operation(summary = "Atualiza um usuário por ID",
			responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID do usuário inválido",
					content = @Content(schema = @Schema(ref = "Problema"))
			),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
					content = @Content(schema = @Schema(ref = "Problema"))
			)
		})
	public UsuarioModel atualizar(
			@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId,  
			@RequestBody(description = "Representação de um usuário com dados atualizados", required = true) UsuarioInput usuarioInput);
	
	
	@Operation(summary = "Atualiza a senha de um usuário", responses = {
			@ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = {
					@Content(schema = @Schema(ref = "Problema")) })
	})
	ResponseEntity<Void> alterarSenha(
			@Parameter(description = "ID do usuário", example = "1", required = true) Long usuarioId,
			@RequestBody(description = "Representação de uma nova senha", required = true) SenhaInput novaSenhaInput);

}
