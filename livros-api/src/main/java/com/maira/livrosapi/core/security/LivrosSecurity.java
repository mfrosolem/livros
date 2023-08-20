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

}
