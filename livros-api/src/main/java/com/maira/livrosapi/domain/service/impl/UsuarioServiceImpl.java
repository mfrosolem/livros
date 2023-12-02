package com.maira.livrosapi.domain.service.impl;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.NegocioException;
import com.maira.livrosapi.domain.exception.UsuarioNaoEncontradoException;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.repository.UsuarioRepository;
import com.maira.livrosapi.domain.service.GrupoService;
import com.maira.livrosapi.domain.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
	
	private static final String MSG_USUARIO_EM_USO = "Usuário de código %d não pode ser removido, está em uso";
	
	private final UsuarioRepository usuarioRepository;
	private final GrupoService grupoService;
	private final PasswordEncoder passwordEncoder;


	@Override
	public Usuario buscarOuFalhar(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(id));
	}
	
	@Transactional
	@Override
	public Usuario salvar(Usuario entidade) {
		usuarioRepository.detach(entidade);

		Long grupoId = entidade.getGrupo().getId();
		Grupo grupo = grupoService.buscarOuFalhar(grupoId);
		entidade.setGrupo(grupo);

		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(entidade.getEmail());
		
		if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(entidade)) {
			throw new NegocioException(
					String.format("Já existe um usuário cadastrado com o e-mail %s", entidade.getEmail()));
		}
		
		if (entidade.isNovo()) {
			entidade.setSenha(passwordEncoder.encode(entidade.getSenha()));
		}
		
		return usuarioRepository.save(entidade);
	}
	
	
	@Transactional
	@Override
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		
		if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
		}
		
		usuario.setSenha(passwordEncoder.encode(novaSenha));
	}
	
	@Transactional
	@Override
	public void excluir(Long id) {
		try {
			usuarioRepository.deleteById(id);
			usuarioRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new UsuarioNaoEncontradoException(id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_USUARIO_EM_USO, id));
		}
	}

	@Override
	public Page<Usuario> listByContaining(String contain, Pageable pageable) {
		return usuarioRepository.findByNomeContaining(contain, pageable);
	}
	

}
