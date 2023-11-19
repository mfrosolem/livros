package com.maira.livrosapi.api.controller;

import com.maira.livrosapi.api.assembler.GrupoModelAssembler;
import com.maira.livrosapi.api.model.GrupoModel;
import com.maira.livrosapi.api.openapi.controller.UsuarioGrupoControllerOpenApi;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.service.UsuarioService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/usuarios/{usuarioId}/grupos")
public class UsuarioGrupoController implements UsuarioGrupoControllerOpenApi {

    private final UsuarioService usuarioService;
    private final GrupoModelAssembler grupoModelAssembler;

    public UsuarioGrupoController(UsuarioService usuarioService, GrupoModelAssembler grupoModelAssembler) {
        this.usuarioService = usuarioService;
        this.grupoModelAssembler = grupoModelAssembler;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GrupoModel> listar(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.buscarOuFalhar(usuarioId);
        List<Grupo> grupos = List.copyOf(usuario.getGrupos());
        return grupoModelAssembler.toCollectionModel(grupos);
    }

    @Override
    @PutMapping("/{grupoId}")
    public ResponseEntity<Void> associar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
        usuarioService.associarGrupo(usuarioId, grupoId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{grupoId}")
    public ResponseEntity<Void> desassociar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
        usuarioService.desassociarGrupo(usuarioId, grupoId);
        return ResponseEntity.noContent().build();
    }
}
