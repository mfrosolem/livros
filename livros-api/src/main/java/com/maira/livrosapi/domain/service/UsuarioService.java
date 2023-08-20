package com.maira.livrosapi.domain.service;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.NegocioException;
import com.maira.livrosapi.domain.exception.UsuarioNaoEncontradoException;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {
	
	private static final String MSG_USUARIO_EM_USO = "Usuário de código %d não pode ser removido, está em uso";
	
	private final UsuarioRepository usuarioRepository;
	
	private final PermissaoService permissaoService;
	
	private final PasswordEncoder passwordEncoder;
	
	
	public Usuario buscarOuFalhar(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		usuarioRepository.detach(usuario);
		
		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
		
		if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new NegocioException(
					String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
		}
		
		if (usuario.isNovo()) {
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		}
		
		return usuarioRepository.save(usuario);
	}
	
	
	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		
		if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
		}
		
		usuario.setSenha(passwordEncoder.encode(novaSenha));
	}
	
	@Transactional
	public void excluir(Long usuarioId) {
		try {
			usuarioRepository.deleteById(usuarioId);
			usuarioRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new UsuarioNaoEncontradoException(usuarioId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_USUARIO_EM_USO, usuarioId));
		}
	}
	
	@Transactional
	public void associarPermissao(Long usuarioId, Long permissaoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Permissao permissao = permissaoService.buscarOuFalhar(permissaoId);
		usuario.adicionarPermissao(permissao);
	}
	
	@Transactional
	public void desassociarPermissao(Long usuarioId, Long permissaoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Permissao permissao = permissaoService.buscarOuFalhar(permissaoId);
		usuario.removerPermissao(permissao);
	}
	

}
