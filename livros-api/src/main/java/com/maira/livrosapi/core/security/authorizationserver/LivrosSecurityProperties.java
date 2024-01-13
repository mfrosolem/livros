package com.maira.livrosapi.core.security.authorizationserver;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

@Component
@Getter
@Setter
@Validated
@ConfigurationProperties("livros.auth")
public class LivrosSecurityProperties {
	
	@NotBlank
	private String providerUrl;

	@NotEmpty
	private List<String> redirectsUriPermitidos;

	@NotEmpty
	private List<String> originsPermitidas = Arrays.asList("http://127.0.0.1:8000");

}
