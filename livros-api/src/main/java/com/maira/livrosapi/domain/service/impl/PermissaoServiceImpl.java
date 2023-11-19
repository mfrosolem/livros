package com.maira.livrosapi.domain.service.impl;

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
public class PermissaoServiceImpl implements com.maira.livrosapi.domain.service.PermissaoService {

	private static final String MSG_PERMISSAO_EM_USO = "Permissão de código %d não pode ser removida, pois está em uso";
	private static final String MSG_PERMISSAO_UNIQUE = "Permissão de nome %s já cadastrada";
	
	private final PermissaoRepository permissaoRepository;
	public PermissaoServiceImpl(PermissaoRepository permissaoRepository) {
		this.permissaoRepository = permissaoRepository;
	}

	@Override
	public Permissao buscarOuFalhar(Long id) {
		return permissaoRepository.findById(id)
				.orElseThrow(() -> new PermissaoNaoEncontradaException(id));
	}
	
	@Transactional
	@Override
	public Permissao salvar(Permissao entidade) {
		try {
			return permissaoRepository.save(entidade);
		} catch (DataIntegrityViolationException ex) {
			throw new EntidadeEmUsoException(String.format(MSG_PERMISSAO_UNIQUE, entidade.getNome()));
		}
	}

	@Transactional
	@Override
	public void excluir(Long id) {
		try {
			permissaoRepository.deleteById(id);
			permissaoRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new PermissaoNaoEncontradaException(id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_PERMISSAO_EM_USO, id));
		}
	}

	@Override
	public Page<Permissao> listByContaining(String contain, Pageable pageable) {
		return permissaoRepository.findByNomeContaining(contain, pageable);
	}
	

}
