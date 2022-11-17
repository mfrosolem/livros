package com.maira.livrosapi.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maira.livrosapi.domain.exception.AutorNaoEncontradoException;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.repository.AutorRepository;

@Service
public class AutorService {

	private static final String MSG_AUTOR_EM_USO = "Autor(a) de código %d não pode ser removido(a), pois está em uso";
	private static final String MSG_AUTOR_UNIQUE = "Autor(a) de nome %s %s já cadastrado(a)";

	@Autowired
	private AutorRepository autorRepository;

	public Autor buscarOuFalhar(Long autorId) {
		return autorRepository.findById(autorId).orElseThrow(() -> new AutorNaoEncontradoException(autorId));
	}

	@Transactional
	public Autor salvar(Autor autor) {
		try {
			return autorRepository.save(autor);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_AUTOR_UNIQUE, autor.getNome(), autor.getSobrenome()));
		}
	}

	@Transactional
	public void excluir(Long autorId) {
		try {
			autorRepository.deleteById(autorId);
			autorRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new AutorNaoEncontradoException(autorId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_AUTOR_EM_USO, autorId));
		}
	}

}
