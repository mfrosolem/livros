package com.maira.livrosapi.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maira.livrosapi.api.assembler.FotoLivroModelAssembler;
import com.maira.livrosapi.api.model.FotoLivroModel;
import com.maira.livrosapi.api.model.input.FotoLivroInput;
import com.maira.livrosapi.api.openapi.controller.LivroFotoControllerOpenApi;
import com.maira.livrosapi.core.security.CheckRoleSecurity;
import com.maira.livrosapi.domain.exception.EntidadeNaoEncontradaException;
import com.maira.livrosapi.domain.model.FotoLivro;
import com.maira.livrosapi.domain.model.Livro;
import com.maira.livrosapi.domain.service.FotoLivroService;
import com.maira.livrosapi.domain.service.FotoStorageService;
import com.maira.livrosapi.domain.service.LivroService;

import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/livros/{livroId}/foto", produces = MediaType.APPLICATION_JSON_VALUE)
public class LivroFotoController implements LivroFotoControllerOpenApi {

	@Autowired
	private FotoLivroService fotoLivroService;

	@Autowired
	private FotoLivroModelAssembler fotoAssembler;

	@Autowired
	private LivroService livroService;

	@Autowired
	private FotoStorageService fotoStorage;

	
	@CheckRoleSecurity.Livros.PodeCadastrarEditar
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoLivroModel atualizarFoto(@PathVariable Long livroId, @Valid FotoLivroInput fotoLivroInput)
			throws IOException {

		Livro livro = livroService.buscarOuFalhar(livroId);

		MultipartFile arquivo = fotoLivroInput.getArquivo();

		FotoLivro foto = new FotoLivro();
		foto.setLivro(livro);
		foto.setDescricao(fotoLivroInput.getDescricao());
		foto.setContentType(arquivo.getContentType());
		foto.setTamanho(arquivo.getSize());
		foto.setNomeArquivo(arquivo.getOriginalFilename());

		FotoLivro fotoSalva = fotoLivroService.salvar(foto, arquivo.getInputStream());

		return fotoAssembler.toModel(fotoSalva);
	}

	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public FotoLivroModel buscar(@PathVariable Long livroId) {
		FotoLivro fotoLivro = fotoLivroService.buscarOuFalhar(livroId);
		return fotoAssembler.toModel(fotoLivro);
	}

	
	@CheckRoleSecurity.Livros.PodeCadastrarEditar
	@GetMapping(produces = MediaType.ALL_VALUE)
	public ResponseEntity<InputStreamResource> servir(@PathVariable Long livroId,
			@RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {

		try {
			FotoLivro fotoLivro = fotoLivroService.buscarOuFalhar(livroId);

			MediaType mediaTypeFoto = MediaType.parseMediaType(fotoLivro.getContentType());
			List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);

			verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);

			InputStream inputStream = fotoStorage.recuperar(fotoLivro.getNomeArquivo());

			return ResponseEntity.ok()
					.contentType(mediaTypeFoto)
					.body(new InputStreamResource(inputStream));

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}

	}

	
	@CheckRoleSecurity.Livros.PodeRemover
	@DeleteMapping
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> remover(@PathVariable Long livroId) {
		
		fotoLivroService.excluir(livroId);
		
		return ResponseEntity.noContent().build();
	}
	

	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediaTypesAceitas)
			throws HttpMediaTypeNotAcceptableException {

		boolean compativel = mediaTypesAceitas.stream()
				.anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

		if (!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
		}
	}

}
