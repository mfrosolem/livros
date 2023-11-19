package com.maira.livrosapi.domain.service.impl;

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
public class AutorServiceImpl implements com.maira.livrosapi.domain.service.AutorService {

	private static final String MSG_AUTOR_EM_USO = "Autor(a) de código %d não pode ser removido(a), pois está em uso";
	private static final String MSG_AUTOR_UNIQUE = "Autor(a) de nome %s %s já cadastrado(a)";

	private final AutorRepository autorRepository;

	public AutorServiceImpl(AutorRepository autorRepository) {
		this.autorRepository = autorRepository;
	}

	@Override
	public Autor buscarOuFalhar(Long id) {
		return autorRepository.findById(id).orElseThrow(() -> new AutorNaoEncontradoException(id));
	}

	@Transactional
	@Override
	public Autor salvar(Autor entidade) {
		try {
			return autorRepository.save(entidade);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_AUTOR_UNIQUE, entidade.getNome(), entidade.getSobrenome()));
		}
	}

	@Transactional
	@Override
	public void excluir(Long id) {
		try {
			autorRepository.deleteById(id);
			autorRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new AutorNaoEncontradoException(id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_AUTOR_EM_USO, id));
		}
	}

	@Override
	public Page<Autor> listByContaining(String contain, Pageable pageable) {
		return autorRepository.findByNomeContaining(contain, pageable);
	}

}
