package com.maira.livrosapi.domain.service;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.util.StringUtils;

import lombok.Builder;
import lombok.Getter;

public interface FotoStorageService {

	InputStream recuperar(String nomeArquivo);

	void armazenar(NovaFoto novaFoto);

	void remover(String nomeArquivo);

	default void substituir(String nomeArquivoAntigo, NovaFoto novaFoto) {

		this.armazenar(novaFoto);

		if (StringUtils.hasText(nomeArquivoAntigo)) {
			this.remover(nomeArquivoAntigo);
		}

	}

	default String gerarNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID().toString() + "_" + nomeOriginal;
	}

	@Builder
	@Getter
	class NovaFoto {
		private String nomeArquivo;
		private InputStream inputStream;
	}

}
