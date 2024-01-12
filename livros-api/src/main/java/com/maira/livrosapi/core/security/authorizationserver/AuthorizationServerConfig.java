package com.maira.livrosapi.core.security.authorizationserver;

import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.repository.UsuarioRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class AuthorizationServerConfig {
	
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		
		return http.formLogin(Customizer.withDefaults()).build();
	}
	
	
	@Bean
	public AuthorizationServerSettings providerSettings() {
		return AuthorizationServerSettings.builder()
				.build();
	}
	
	
	@Bean
	public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {

		RegisteredClient livrosbackend = RegisteredClient
				.withId("1")
				.clientId("livros-backend")
				.clientSecret(passwordEncoder.encode("backend123"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.scope("READ")
				.scope("WRITE")
				.scope("DELETE")
				.tokenSettings(TokenSettings.builder()
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
						.accessTokenTimeToLive(Duration.ofMinutes(30))
						.build())
				.build();

		
		//Fluxo Authorization Code para Front-end e Documentação Swagger
		RegisteredClient livrosWeb = RegisteredClient
				.withId("2")
				.clientId("livros-frontend")
				.clientSecret(passwordEncoder.encode("frontend123"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.scope("READ")
				.scope("WRITE")
				.scope("DELETE")
				.tokenSettings(TokenSettings.builder()
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
						.authorizationCodeTimeToLive(Duration.ofMinutes(5))
						.accessTokenTimeToLive(Duration.ofMinutes(30))
						.refreshTokenTimeToLive(Duration.ofDays(30))
						.build())
				.redirectUri("http://local-livros.com:8000/authorized")
				.redirectUri("http://local-livros.com:8080/swagger-ui/oauth2-redirect.html")
				.clientSettings(ClientSettings.builder()
						.requireAuthorizationConsent(false)
						.build())
				.build();
		
		return new InMemoryRegisteredClientRepository(Arrays.asList(livrosbackend, livrosWeb));
	}
	
	
	/*Configuracao de bean de assinatura JWT*/
	@Bean
	public JWKSource<SecurityContext> jwkSource(JwtKeyStoreProperties properties) throws Exception {
		char[] keyStorePass = properties.getPassword().toCharArray();
		String keypairAlias = properties.getKeypairAlias();
		
		Resource jksLocation = properties.getJksLocation();
		InputStream inputStream = jksLocation.getInputStream();
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, keyStorePass);
		
		RSAKey rsaKey = RSAKey.load(keyStore, keypairAlias, keyStorePass);
		
		return new ImmutableJWKSet<>(new JWKSet(rsaKey));
	}
	
	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UsuarioRepository usuarioRepository) {
		return context -> {
			Authentication authentication = context.getPrincipal();
			if (authentication.getPrincipal() instanceof User) {
				User user = (User) authentication.getPrincipal();
				
				Usuario usuario = usuarioRepository.findByEmail(user.getUsername()).orElseThrow();
				
				Set<String> authorities = new HashSet<>();
				for(GrantedAuthority authority : user.getAuthorities()) {
					authorities.add(authority.getAuthority());
				}
				
				context.getClaims().claim("usuario_id", usuario.getId().toString());
				context.getClaims().claim("nome", usuario.getNome().toString());
				context.getClaims().claim("primeiro_acesso", usuario.getPrimeiroAcesso());
				context.getClaims().claim("authorities", authorities);
			}
		};
	}
	

}
