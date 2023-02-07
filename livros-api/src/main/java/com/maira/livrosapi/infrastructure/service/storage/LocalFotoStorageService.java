package com.maira.livrosapi.infrastructure.service.storage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.maira.livrosapi.domain.service.FotoStorageService;

@Service
public class LocalFotoStorageService implements FotoStorageService {

	@Value("${livrosapi.storage.local.diretorio-fotos}")
	private Path diretorioFotos;

	@Override
	public InputStream recuperar(String nomeArquivo) {
		try {
			Path pathArquivo = getArquivoPath(nomeArquivo);
			return Files.newInputStream(pathArquivo);
		} catch (Exception e) {
			throw new StorageException("Não foi possível recuperar arquivo.", e);
		}
	}

	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());

			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar arquivo.", e);
		}

	}

	@Override
	public void remover(String nomeArquivo) {

		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir arquivo", e);
		}

	}

	// retorna o caminho completo onde o arquivo ficará
	private Path getArquivoPath(String nomeArquivo) {
		return diretorioFotos.resolve(Path.of(nomeArquivo));
	}

}
