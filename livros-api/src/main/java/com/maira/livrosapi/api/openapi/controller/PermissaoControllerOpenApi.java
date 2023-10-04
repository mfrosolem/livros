package com.maira.livrosapi.api.openapi.controller;

import java.util.List;

import com.maira.livrosapi.api.model.PermissaoModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Permissões")
public interface PermissaoControllerOpenApi {
	
	@Operation(summary = "Lista todas as permissões cadastradas")
	List<PermissaoModel> listar();

}
