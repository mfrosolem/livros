package com.maira.livrosapi.domain.service;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maira.livrosapi.domain.exception.FotoLivroNaoEncontradaException;
import com.maira.livrosapi.domain.model.FotoLivro;
import com.maira.livrosapi.domain.repository.LivroRepository;
import com.maira.livrosapi.domain.service.FotoStorageService.NovaFoto;

@Service
public class FotoLivroService {

	@Autowired
	private LivroRepository livroRepository;

	@Autowired
	private FotoStorageService fotoStorage;

	@Transactional
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

	public FotoLivro buscarOuFalhar(Long livroId) {
		return livroRepository.findFotoById(livroId).orElseThrow(() -> new FotoLivroNaoEncontradaException(livroId));
	}

	@Transactional
	public void excluir(Long livroId) {
		FotoLivro foto = this.buscarOuFalhar(livroId);
		String nomeArquivo = foto.getNomeArquivo();

		livroRepository.delete(foto);
		livroRepository.flush();

		fotoStorage.remover(nomeArquivo);
	}

}
