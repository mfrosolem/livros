package com.maira.livrosapi.domain.service.impl;

import com.maira.livrosapi.domain.exception.EditoraNaoEncontradaException;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.repository.EditoraRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditoraServiceImpl implements com.maira.livrosapi.domain.service.EditoraService {

	private static final String MSG_EDITORA_EM_USO = "Editora de código %d não pode ser removida, pois está em uso";
	private static final String MSG_EDITORA_UNIQUE = "Editora de nome %s já cadastrada";

	private final EditoraRepository editoraRepository;

	public EditoraServiceImpl(EditoraRepository editoraRepository) {
		this.editoraRepository = editoraRepository;
	}

	@Override
	public Editora buscarOuFalhar(Long id) {
		return editoraRepository.findById(id).orElseThrow(() -> new EditoraNaoEncontradaException(id));
	}

	@Transactional
	@Override
	public Editora salvar(Editora entidade) {
		try {
			return editoraRepository.save(entidade);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_EDITORA_UNIQUE, entidade.getNome()));
		}
		
	}

	@Transactional
	@Override
	public void excluir(Long id) {
		try {
			editoraRepository.deleteById(id);
			editoraRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new EditoraNaoEncontradaException(id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_EDITORA_EM_USO, id));
		}
	}

	@Override
	public Page<Editora> listByContaining(String contain, Pageable pageable) {
		return editoraRepository.findByNomeContaining(contain, pageable);
	}

}
