package com.maira.livrosapi.core.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class ResourceServerConfig {
	
	@Bean
	//@Order(2)
	public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {

		http
				.csrf(csrf -> csrf.disable())
				.formLogin(Customizer.withDefaults())
				.authorizeHttpRequests(authorizeConfig -> {
					authorizeConfig.anyRequest().authenticated();

				})
				.oauth2ResourceServer(configJwt -> {
					configJwt.jwt((conv) -> {
						conv.jwtAuthenticationConverter(jwtAuthenticationConverter());
					});
				});

		http.logout(logoutConfig -> {
			logoutConfig.logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
				String returnTo = httpServletRequest.getParameter("returnTo");

				if (!StringUtils.hasText(returnTo)) {
					returnTo = "http://localhost:8080";
				}

				httpServletResponse.setStatus(302);
				httpServletResponse.sendRedirect(returnTo);
			});
		});


		return http.build();
	}
	
	@Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }
	
	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

		converter.setJwtGrantedAuthoritiesConverter(jwt -> {

			//"authorities" e um campo costumizado no AuthorizationServerConfig
			List<String> authorities = jwt.getClaimAsStringList("authorities");

			//JwtGrantedAuthoritiesConverter converte os Scopes do JWT em Authority
			JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
			Collection<GrantedAuthority> grantedAuthorities = authoritiesConverter.convert(jwt);

			//Se for Client Credentials, a authorities é null
			//Logo, deve retornar grantedAuthorities que contem os SCOPES
			if (Objects.isNull(authorities)) {
				return grantedAuthorities; 	//Evita erro de serializacao durante a leitura do token
			}

			//Combina as duas linhas: a que vem dos scopes de JWT (grantedAuthorities) e as que vem dos "authorities" do JWT
			grantedAuthorities.addAll(authorities
					.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList()));

			return grantedAuthorities;
		});

		return converter;
    }

	


}
