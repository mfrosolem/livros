package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GeneroNaoEncontradoException;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.repository.GeneroRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeneroService {

	private static final String MSG_GENERO_EM_USO = "Genero de código %d não pode ser removido, pois está em uso";
	private static final String MSG_GENERO_UNIQUE = "Genero de descrição %s já cadastrado";

	private final GeneroRepository generoRepository;

	public GeneroService(GeneroRepository generoRepository) {
		this.generoRepository = generoRepository;
	}

	public Genero buscarOuFalhar(Long generoId) {
		return generoRepository.findById(generoId).orElseThrow(() -> new GeneroNaoEncontradoException(generoId));
	}

	@Transactional
	public Genero salvar(Genero genero) {
		try {
			return generoRepository.save(genero);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_GENERO_UNIQUE, genero.getDescricao()));
		}
	}

	@Transactional
	public void excluir(Long generoId) {

		try {
			generoRepository.deleteById(generoId);
			generoRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new GeneroNaoEncontradoException(generoId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_GENERO_EM_USO, generoId));
		}

	}

	public Page<Genero> listByDescricaoContaining(String descricao, Pageable pageable) {
		return generoRepository.findByDescricaoContaining(descricao, pageable);
	}

}
