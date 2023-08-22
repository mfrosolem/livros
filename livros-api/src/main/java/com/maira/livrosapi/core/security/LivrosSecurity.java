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
	
	public boolean hasAuthority(String authorityName) {
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(authorityName));
	}
	
	public boolean podeConsultarGeneros() {
		return isAuthenticated() && hasAuthority("ROLE_GENERO_PESQUISAR");
	}
	
	public boolean podeCadastrarEditarGenero() {
		return isAuthenticated() && hasAuthority("ROLE_GENERO_CADASTRAR");
	}
	
	public boolean podeRemoverGenero() {
		return isAuthenticated() && hasAuthority("ROLE_GENERO_REMOVER");
	}
	
	public boolean podeConsultarEditoras() {
		return isAuthenticated() && hasAuthority("ROLE_EDITORA_PESQUISAR");
	}
	
	public boolean podeCadastrarEditarEditora() {
		return isAuthenticated() && hasAuthority("ROLE_EDITORA_CADASTRAR");
	}
	
	public boolean podeRemoverEditora() {
		return isAuthenticated() && hasAuthority("ROLE_EDITORA_REMOVER");
	}
	
	public boolean podeConsultarAutores() {
		return isAuthenticated() && hasAuthority("ROLE_AUTOR_PESQUISAR");
	}
	
	public boolean podeCadastrarEditarAutor() {
		return isAuthenticated() && hasAuthority("ROLE_AUTOR_CADASTRAR");
	}
	
	public boolean podeRemoverAutor() {
		return isAuthenticated() && hasAuthority("ROLE_AUTOR_REMOVER");
	}
	
	public boolean podeConsultarLivros() {
		return isAuthenticated() && hasAuthority("ROLE_LIVRO_PESQUISAR");
	}
	
	public boolean podeCadastrarEditarLivro() {
		return isAuthenticated() && hasAuthority("ROLE_LIVRO_CADASTRAR");
	}
	
	public boolean podeRemoverLivro() {
		return isAuthenticated() && hasAuthority("ROLE_LIVRO_REMOVER");
	}
	
	

}
