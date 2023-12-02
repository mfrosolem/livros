package com.maira.livrosapi.api.controller;

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
import com.maira.livrosapi.domain.service.FotoStorageService.FotoRecuperada;
import com.maira.livrosapi.domain.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping(value = "/livros/{livroId}/foto", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class LivroFotoController implements LivroFotoControllerOpenApi {

	private final FotoLivroService fotoLivroService;
	private final FotoLivroModelAssembler fotoAssembler;
	private final LivroService livroService;
	private final FotoStorageService fotoStorage;

	
	@CheckRoleSecurity.Livros.PodeCadastrarEditar
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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


	@GetMapping(produces = MediaType.ALL_VALUE)
	public ResponseEntity<InputStreamResource> servir(@PathVariable Long livroId,
			@RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {

		try {
			FotoLivro fotoLivro = fotoLivroService.buscarOuFalhar(livroId);
			List<String> listMedia = Arrays.stream(acceptHeader.split(";")).toList();

			MediaType mediaTypeFoto = MediaType.parseMediaType(fotoLivro.getContentType());
			List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(listMedia);

			verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);

			FotoRecuperada fotoRecuperada = fotoStorage.recuperar(fotoLivro.getNomeArquivo());

			return ResponseEntity.ok()
					.contentType(mediaTypeFoto)
					.body(new InputStreamResource(fotoRecuperada.getInputStream()));

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
