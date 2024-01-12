package com.maira.livrosapi.api.controller;

import com.maira.livrosapi.api.ResourceUriHelper;
import com.maira.livrosapi.api.assembler.UsuarioInputDisassembler;
import com.maira.livrosapi.api.assembler.UsuarioModelAssembler;
import com.maira.livrosapi.api.model.UsuarioModel;
import com.maira.livrosapi.api.model.input.SenhaInput;
import com.maira.livrosapi.api.model.input.UsuarioInput;
import com.maira.livrosapi.api.openapi.controller.UsuarioControllerOpenApi;
import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UsuarioController implements UsuarioControllerOpenApi{
	

	private final UsuarioService cadastroUsuario;
	private final UsuarioInputDisassembler usuarioInputDisassembler;
	private final UsuarioModelAssembler usuarioModelAssembler;
	
	
	@GetMapping
	@Override
	public Page<UsuarioModel> listar(@RequestParam(required = false, defaultValue = "") String nome, Pageable pageable) {
		Page<Usuario> usuariosPage = cadastroUsuario.listByContaining(nome, pageable);
		List<UsuarioModel> usuariosModel = usuarioModelAssembler.toCollectionModel(usuariosPage.getContent());
		return new PageImpl<>(usuariosModel, pageable, usuariosPage.getTotalElements());
	}

	@GetMapping(path = "/{usuarioId}")
	@Override
	public UsuarioModel buscar(@PathVariable Long usuarioId) {
		var usuario = cadastroUsuario.buscarOuFalhar(usuarioId);
		return usuarioModelAssembler.toModel(usuario);
	}

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	@Override
	public UsuarioModel adicionar(@RequestBody @Valid UsuarioInput usuarioInput) {
		var usuario = usuarioInputDisassembler.toDomainObject(usuarioInput);
		usuario = cadastroUsuario.salvar(usuario);
		var usuarioModel = usuarioModelAssembler.toModel(usuario);
		
		ResourceUriHelper.addUriInResponseHeader(usuarioModel.getId());
		
		return usuarioModel;
	}

	@PutMapping(path = "/{usuarioId}")
	@Override
	public UsuarioModel atualizar(@PathVariable Long usuarioId, 
			@RequestBody @Valid UsuarioInput usuarioInput) {
		
		var usuario = cadastroUsuario.buscarOuFalhar(usuarioId);
		usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuario);
		usuario = cadastroUsuario.salvar(usuario);
		
		return usuarioModelAssembler.toModel(usuario);
	}

	@PutMapping(path = "/{usuarioId}/senha")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@Override
	public ResponseEntity<Void> alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput novaSenhaInput) {
		cadastroUsuario.alterarSenha(usuarioId, novaSenhaInput.getSenhaAtual(), novaSenhaInput.getNovaSenha());
		return ResponseEntity.noContent().build();
	}

	
	
}
