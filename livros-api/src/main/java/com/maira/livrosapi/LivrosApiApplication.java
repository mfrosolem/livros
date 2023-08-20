package com.maira.livrosapi;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.maira.livrosapi.core.io.Base64ProtocolResolver;
import com.maira.livrosapi.infrastructure.repository.CustomJpaRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class LivrosApiApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		var app = new SpringApplication(LivrosApiApplication.class);
		app.addListeners(new Base64ProtocolResolver());
		app.run(args);
	}

}
