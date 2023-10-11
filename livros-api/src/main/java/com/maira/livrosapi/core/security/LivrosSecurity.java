package com.maira.livrosapi.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class LivrosSecurity {
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public boolean isAuthenticated() {
		return getAuthentication().isAuthenticated();
	}
	
	public Long getUsuarioId() {
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();

		Object usuarioId = jwt.getClaim("usuario_id");

		if (usuarioId == null) {
			return null;
		}

		return Long.valueOf(usuarioId.toString());
	}
	
	public boolean usuarioAutenticadoIgual(Long usuarioId) {
		return getUsuarioId() != null && usuarioId != null 
				&& getUsuarioId().equals(usuarioId);
	}
	
	public boolean hasAuthority(String authorityName) {
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(authorityName));
	}
	
	public boolean podeConsultarGeneros() {
		return isAuthenticated() && hasAuthority("CONSULTAR_GENERO");
	}
	
	public boolean podeCadastrarEditarGenero() {
		return isAuthenticated() && hasAuthority("CADASTRAR_GENERO");
	}
	
	public boolean podeRemoverGenero() {
		return isAuthenticated() && hasAuthority("REMOVER_GENERO");
	}
	
	public boolean podeConsultarEditoras() {
		return isAuthenticated() && hasAuthority("CONSULTAR_EDITORA");
	}
	
	public boolean podeCadastrarEditarEditora() {
		return isAuthenticated() && hasAuthority("CADASTRAR_EDITORA");
	}
	
	public boolean podeRemoverEditora() {
		return isAuthenticated() && hasAuthority("REMOVER_EDITORA");
	}
	
	public boolean podeConsultarAutores() {
		return isAuthenticated() && hasAuthority("CONSULTAR_AUTOR");
	}
	
	public boolean podeCadastrarEditarAutor() {
		return isAuthenticated() && hasAuthority("CADASTRAR_AUTOR");
	}
	
	public boolean podeRemoverAutor() {
		return isAuthenticated() && hasAuthority("REMOVER_AUTOR");
	}
	
	public boolean podeConsultarLivros() {
		return isAuthenticated() && hasAuthority("CONSULTAR_LIVRO");
	}
	
	public boolean podeCadastrarEditarLivro() {
		return isAuthenticated() && hasAuthority("CADASTRAR_LIVRO");
	}
	
	public boolean podeRemoverLivro() {
		return isAuthenticated() && hasAuthority("REMOVER_LIVRO");
	}

	public boolean podeConsultarUsuariosGruposPermissoes() {
		return isAuthenticated() && hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
	}

	public boolean podeEditarUsuariosGruposPermissoes() {
		return isAuthenticated() && hasAuthority("CADASTRAR_USUARIOS_GRUPOS_PERMISSOES");
	}

}
