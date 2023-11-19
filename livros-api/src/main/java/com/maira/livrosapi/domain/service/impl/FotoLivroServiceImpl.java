package com.maira.livrosapi.domain.service.impl;

import com.maira.livrosapi.domain.exception.FotoLivroNaoEncontradaException;
import com.maira.livrosapi.domain.model.FotoLivro;
import com.maira.livrosapi.domain.repository.LivroRepository;
import com.maira.livrosapi.domain.service.FotoLivroService;
import com.maira.livrosapi.domain.service.FotoStorageService;
import com.maira.livrosapi.domain.service.FotoStorageService.NovaFoto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Optional;

@Service
public class FotoLivroServiceImpl implements FotoLivroService {

	private final LivroRepository livroRepository;
	private final FotoStorageService fotoStorage;

	public FotoLivroServiceImpl(LivroRepository livroRepository, FotoStorageService fotoStorage) {
		this.livroRepository = livroRepository;
		this.fotoStorage = fotoStorage;
	}

	@Transactional
	@Override
	public FotoLivro salvar(FotoLivro foto, InputStream dadosArquivos) {

		Long livroId = foto.getLivro().getId();
		String nomeNovoArquivo = fotoStorage.gerarNomeArquivo(foto.getNomeArquivo());
		String nomeArquivoExistente = "";

		Optional<FotoLivro> fotoExistente = livroRepository.findFotoById(livroId);

		if (fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			livroRepository.delete(fotoExistente.get());
		}

		foto.setNomeArquivo(nomeNovoArquivo);
		foto = livroRepository.save(foto);
		livroRepository.flush();

		NovaFoto novaFoto = NovaFoto.builder().nomeArquivo(foto.getNomeArquivo()).inputStream(dadosArquivos).build();
		fotoStorage.substituir(nomeArquivoExistente, novaFoto);

		return foto;

	}

	@Override
	public FotoLivro buscarOuFalhar(Long livroId) {
		return livroRepository.findFotoById(livroId).orElseThrow(() -> new FotoLivroNaoEncontradaException(livroId));
	}

	@Transactional
	@Override
	public void excluir(Long livroId) {
		FotoLivro foto = this.buscarOuFalhar(livroId);
		String nomeArquivo = foto.getNomeArquivo();

		livroRepository.delete(foto);
		livroRepository.flush();

		fotoStorage.remover(nomeArquivo);
	}

}
