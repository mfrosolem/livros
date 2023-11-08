package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.AutorNaoEncontradoException;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.repository.AutorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorService {

	private static final String MSG_AUTOR_EM_USO = "Autor(a) de código %d não pode ser removido(a), pois está em uso";
	private static final String MSG_AUTOR_UNIQUE = "Autor(a) de nome %s %s já cadastrado(a)";

	private final AutorRepository autorRepository;

	public AutorService(AutorRepository autorRepository) {
		this.autorRepository = autorRepository;
	}

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

	public Page<Autor> listByNameContaining(String name, Pageable pageable) {
		return autorRepository.findByNomeContaining(name, pageable);
	}

}
