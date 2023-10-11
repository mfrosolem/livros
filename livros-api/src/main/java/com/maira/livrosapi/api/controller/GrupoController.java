package com.maira.livrosapi.api.controller;

import com.maira.livrosapi.api.ResourceUriHelper;
import com.maira.livrosapi.api.assembler.GrupoInputDisassembler;
import com.maira.livrosapi.api.assembler.GrupoModelAssembler;
import com.maira.livrosapi.api.model.GrupoModel;
import com.maira.livrosapi.api.model.input.GrupoInput;
import com.maira.livrosapi.api.openapi.controller.GrupoControllerOpenApi;
import com.maira.livrosapi.core.security.CheckRoleSecurity;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.repository.GrupoRepository;
import com.maira.livrosapi.domain.service.GrupoService;
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
@RequestMapping(path = "/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class GrupoController implements GrupoControllerOpenApi {

    private final GrupoRepository grupoRepository;

    private final GrupoService cadastroGrupo;

    private final GrupoInputDisassembler grupoInputDisassembler;

    private final GrupoModelAssembler grupoModelAssembler;

    @Override
    @CheckRoleSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public Page<GrupoModel> listar(@RequestParam(required = false, defaultValue = "") String nome, Pageable pageable) {
        Page<Grupo> gruposPage = grupoRepository.findByNomeContaining(nome, pageable);
        List<GrupoModel> gruposModel = grupoModelAssembler.toCollectionModel(gruposPage.getContent());
        Page<GrupoModel> gruposModelPage = new PageImpl<>(gruposModel, pageable, gruposPage.getTotalElements());
        return gruposModelPage;
    }

    @Override
    @CheckRoleSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping(value = "/{grupoId}")
    public GrupoModel buscar(@PathVariable Long grupoId) {
        var grupo = cadastroGrupo.buscarOuFalhar(grupoId);
        return grupoModelAssembler.toModel(grupo);
    }

    @Override
    @CheckRoleSecurity.UsuariosGruposPermissoes.PodeCadastrarEditar
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);
        grupo = cadastroGrupo.salvar(grupo);
        GrupoModel grupoModel = grupoModelAssembler.toModel(grupo);

        ResourceUriHelper.addUriInResponseHeader(grupoModel.getId());

        return grupoModel;
    }

    @Override
    @CheckRoleSecurity.UsuariosGruposPermissoes.PodeCadastrarEditar
    @PutMapping(value = "/{grupoId}")
    public GrupoModel atualizar(@PathVariable Long grupoId, @RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupoAtual = cadastroGrupo.buscarOuFalhar(grupoId);
        grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);
        grupoAtual = cadastroGrupo.salvar(grupoAtual);
        return grupoModelAssembler.toModel(grupoAtual);
    }

    @Override
    @CheckRoleSecurity.UsuariosGruposPermissoes.PodeCadastrarEditar
    @DeleteMapping(value = "/{grupoId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long grupoId) {
        cadastroGrupo.excluir(grupoId);
    }
}
