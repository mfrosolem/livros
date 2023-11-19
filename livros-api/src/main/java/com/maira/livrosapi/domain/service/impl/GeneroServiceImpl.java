package com.maira.livrosapi.domain.service.impl;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GeneroNaoEncontradoException;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.repository.GeneroRepository;
import com.maira.livrosapi.domain.service.GeneroService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeneroServiceImpl implements GeneroService {

    private static final String MSG_GENERO_EM_USO = "Genero de código %d não pode ser removido, pois está em uso";
    private static final String MSG_GENERO_UNIQUE = "Genero de descrição %s já cadastrado";

    private final GeneroRepository generoRepository;

    public GeneroServiceImpl(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @Override
    public Genero buscarOuFalhar(Long id) {
        return generoRepository.findById(id).orElseThrow(() -> new GeneroNaoEncontradoException(id));
    }

    @Transactional
    @Override
    public Genero salvar(Genero entidade) {
        try {
            return generoRepository.save(entidade);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_GENERO_UNIQUE, entidade.getDescricao()));
        }
    }

    @Transactional
    @Override
    public void excluir(Long id) {
        try {
            generoRepository.deleteById(id);
            generoRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new GeneroNaoEncontradoException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_GENERO_EM_USO, id));
        }
    }

    @Override
    public Page<Genero> listByContaining(String contain, Pageable pageable) {
        return generoRepository.findByDescricaoContaining(contain, pageable);
    }
}
