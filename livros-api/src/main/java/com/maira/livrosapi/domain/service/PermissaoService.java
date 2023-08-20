package com.maira.livrosapi.domain.service;

import org.springframework.stereotype.Service;

import com.maira.livrosapi.domain.exception.PermissaoNaoEncontradaException;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.repository.PermissaoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissaoService {
	
	private final PermissaoRepository permissaoRepository;
	
	
	public Permissao buscarOuFalhar(Long permissaoId) {
		return permissaoRepository.findById(permissaoId)
				.orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
	}
	
	@Transactional
	public Permissao salvar(Permissao permissao) {
		return permissaoRepository.save(permissao);
	}
	

}
