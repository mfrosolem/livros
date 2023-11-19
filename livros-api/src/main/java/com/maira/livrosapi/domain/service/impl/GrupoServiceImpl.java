package com.maira.livrosapi.domain.service.impl;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GrupoNaoEncontradoException;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.repository.GrupoRepository;
import com.maira.livrosapi.domain.service.GrupoService;
import com.maira.livrosapi.domain.service.PermissaoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GrupoServiceImpl implements GrupoService {

    private static final String MSG_GRUPO_EM_USO = "Grupo de código %d não pode ser removido, está em uso";
    private static final String MSG_GRUPO_UNIQUE = "Grupo de nome %s já cadastrado";

    private final GrupoRepository grupoRepository;
    private final PermissaoService permissaoService;

    public GrupoServiceImpl(GrupoRepository grupoRepository, PermissaoService permissaoService) {
        this.grupoRepository = grupoRepository;
        this.permissaoService = permissaoService;
    }

    @Override
    public Grupo buscarOuFalhar(Long id) {
        return grupoRepository.findById(id)
                .orElseThrow(() -> new GrupoNaoEncontradoException(id));
    }

    @Transactional
    @Override
    public Grupo salvar(Grupo entidade) {
        try {
        return grupoRepository.save(entidade);
        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeEmUsoException(String.format(MSG_GRUPO_UNIQUE, entidade.getNome()));
        }
    }

    @Transactional
    @Override
    public void excluir(Long id) {
        try {
            grupoRepository.deleteById(id);
            grupoRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new GrupoNaoEncontradoException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_GRUPO_EM_USO, id));
        }
    }

    @Transactional
    @Override
    public void associarPermissao(Long grupoId, Long permissaoId) {
        Grupo grupo = buscarOuFalhar(grupoId);
        Permissao permissao = permissaoService.buscarOuFalhar(permissaoId);
        grupo.adicionarPermissao(permissao);
    }

    @Transactional
    @Override
    public void desassociarPermissao(Long grupoId, Long permissaoId) {
        Grupo grupo = buscarOuFalhar(grupoId);
        Permissao permissao = permissaoService.buscarOuFalhar(permissaoId);
        grupo.removerPermissao(permissao);
    }

    @Override
    public Page<Grupo> listByContaining(String contain, Pageable pageable) {
        return grupoRepository.findByNomeContaining(contain, pageable);
    }
}
