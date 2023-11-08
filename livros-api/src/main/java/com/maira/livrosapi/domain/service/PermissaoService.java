package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.PermissaoNaoEncontradaException;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.repository.PermissaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PermissaoService {

	private static final String MSG_PERMISSAO_EM_USO = "Permissão de código %d não pode ser removida, pois está em uso";
	private static final String MSG_PERMISSAO_UNIQUE = "Permissão de nome %s já cadastrada";
	
	private final PermissaoRepository permissaoRepository;
	public PermissaoService(PermissaoRepository permissaoRepository) {
		this.permissaoRepository = permissaoRepository;
	}

	public Permissao buscarOuFalhar(Long permissaoId) {
		return permissaoRepository.findById(permissaoId)
				.orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
	}
	
	@Transactional
	public Permissao salvar(Permissao permissao) {
		try {
			return permissaoRepository.save(permissao);
		} catch (DataIntegrityViolationException ex) {
			throw new EntidadeEmUsoException(String.format(MSG_PERMISSAO_UNIQUE, permissao.getNome()));
		}
	}

	@Transactional
	public void excluir(Long permissaoId) {
		try {
			permissaoRepository.deleteById(permissaoId);
			permissaoRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new PermissaoNaoEncontradaException(permissaoId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_PERMISSAO_EM_USO, permissaoId));
		}
	}

	public Page<Permissao> listByNameContaining(String name, Pageable pageable) {
		return permissaoRepository.findByNomeContaining(name, pageable);
	}
	

}
