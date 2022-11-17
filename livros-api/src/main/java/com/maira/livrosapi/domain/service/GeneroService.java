package com.maira.livrosapi.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GeneroNaoEncontradoException;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.repository.GeneroRepository;

@Service
public class GeneroService {

	private static final String MSG_GENERO_EM_USO = "Genero de código %d não pode ser removido, pois está em uso";
	private static final String MSG_GENERO_UNIQUE = "Genero de descrição %s já cadastrado";

	@Autowired
	private GeneroRepository generoRepository;

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

}
