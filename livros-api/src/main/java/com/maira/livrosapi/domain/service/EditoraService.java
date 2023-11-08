package com.maira.livrosapi.domain.service;

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
public class EditoraService {

	private static final String MSG_EDITORA_EM_USO = "Editora de código %d não pode ser removida, pois está em uso";
	private static final String MSG_EDITORA_UNIQUE = "Editora de nome %s já cadastrada";

	private final EditoraRepository editoraRepository;

	public EditoraService(EditoraRepository editoraRepository) {
		this.editoraRepository = editoraRepository;
	}

	public Editora buscarOuFalhar(Long editoraId) {
		return editoraRepository.findById(editoraId).orElseThrow(() -> new EditoraNaoEncontradaException(editoraId));
	}

	@Transactional
	public Editora salvar(Editora editora) {
		try {
			return editoraRepository.save(editora);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_EDITORA_UNIQUE, editora.getNome()));
		}
		
	}

	@Transactional
	public void excluir(Long editoraId) {
		try {
			editoraRepository.deleteById(editoraId);
			editoraRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new EditoraNaoEncontradaException(editoraId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_EDITORA_EM_USO, editoraId));
		}
	}

	public Page<Editora> listByNameContaining(String name, Pageable pageable) {
		return editoraRepository.findByNomeContaining(name, pageable);
	}

}
