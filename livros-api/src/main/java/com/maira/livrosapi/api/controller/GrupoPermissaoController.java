package com.maira.livrosapi.api.controller;

import com.maira.livrosapi.api.assembler.PermissaoModelAssembler;
import com.maira.livrosapi.api.model.PermissaoModel;
import com.maira.livrosapi.api.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.service.GrupoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {

    private final GrupoService grupoService;
    private final PermissaoModelAssembler permissaoModelAssembler;

    public GrupoPermissaoController(GrupoService grupoService, PermissaoModelAssembler permissaoModelAssembler) {
        this.grupoService = grupoService;
        this.permissaoModelAssembler = permissaoModelAssembler;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PermissaoModel> listar(@PathVariable Long grupoId) {
        Grupo grupo = grupoService.buscarOuFalhar(grupoId);
        List<Permissao> permissoes = List.copyOf(grupo.getPermissoes());
        return permissaoModelAssembler.toCollectionModel(permissoes);
    }

    @Override
    @PutMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        grupoService.associarPermissao(grupoId, permissaoId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        grupoService.desassociarPermissao(grupoId, permissaoId);
        return ResponseEntity.noContent().build();
    }
}
