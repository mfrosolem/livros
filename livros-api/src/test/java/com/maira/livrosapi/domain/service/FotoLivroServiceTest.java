package com.maira.livrosapi.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.maira.livrosapi.domain.exception.FotoLivroNaoEncontradaException;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.model.FotoLivro;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.model.Livro;
import com.maira.livrosapi.domain.repository.LivroRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class FotoLivroServiceTest {
	
	@InjectMocks
	FotoLivroService service;
	
	@Mock
	LivroRepository repository;
	
	@Mock
	FotoStorageService fotoStorage;
	
	FotoLivro fotoLivro;
	
	Long livroId;
	
	
	@BeforeEach
	void init() {
		livroId = 1L;
		
		Livro livro = Livro.builder().id(livroId).isbn("9788582850350")
				.titulo("Dom Casmurro").idioma("Português").paginas(390L).ano(2016L)
				.genero(
						Genero.builder().id(1L)
						.descricao("Romance").build())
				.editora(Editora.builder().id(1L).
						nome("Penguin & Companhia das Letras").build())
				.autor(Autor.builder().id(1L)
						.nome("Joaquim Maria").sobrenome("Machado de Assis")
						.nomeConhecido("Machado de Assis").sexo("M").build())
				.build();
		
		fotoLivro = FotoLivro.builder().id(livroId)
				.nomeArquivo("759def9-6d60-4d84-ba57-3faad8930330")
				.descricao("Capa_"+livroId).contentType("image/jpeg")
				.tamanho(678L).livro(livro)
				.build();
	}
	
	
	@Test
	void Dado_um_livroId_valido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_um_fotoLivro_com_id() {
		Mockito.when(repository.findFotoById(Mockito.anyLong()))
			.thenAnswer(answer -> {
				Long livroIdPassado = answer.getArgument(0, Long.class);
				fotoLivro.setId(livroIdPassado);
				return Optional.of(fotoLivro);
			});
		
		FotoLivro fotoLivroRetornado = service.buscarOuFalhar(livroId);
		
		assertInstanceOf(FotoLivro.class, fotoLivroRetornado);
		assertEquals(livroId, fotoLivroRetornado.getId());
		Mockito.verify(repository, Mockito.times(1)).findFotoById(livroId);
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	void Dado_um_livroId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_FotoLivroNaoEncontradoException() {
		Mockito.when(repository.findFotoById(Mockito.anyLong()))
			.thenAnswer(answer -> {
				Long livroIdPassado = answer.getArgument(0, Long.class);
				throw new FotoLivroNaoEncontradaException(livroIdPassado);
			});
		
		final FotoLivroNaoEncontradaException e = 
				assertThrows(FotoLivroNaoEncontradaException.class, () -> service.buscarOuFalhar(livroId));
		
		assertEquals(e.getMessage(), String.format("Não existe um cadastro de foto para o livro com código %d", livroId));
		Mockito.verify(repository, Mockito.times(1)).findFotoById(livroId);
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	void Dado_um_livroId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_fotoLivro () {
		Mockito.when(repository.findFotoById(Mockito.anyLong()))
		.thenAnswer(answer -> {
			Long livroIdPassado = answer.getArgument(0, Long.class);
			fotoLivro.setId(livroIdPassado);
			return Optional.of(fotoLivro);
		});
		Mockito.doNothing().when(repository).delete(Mockito.any(FotoLivro.class));
		Mockito.doNothing().when(repository).flush();
		Mockito.doNothing().when(fotoStorage).remover(fotoLivro.getNomeArquivo());
		
		service.excluir(livroId);
		
		Mockito.verify(repository, Mockito.times(1)).delete(fotoLivro);
		Mockito.verify(repository, Mockito.times(1)).flush();
		Mockito.verifyNoMoreInteractions(repository);
		
		Mockito.verify(fotoStorage, Mockito.times(1)).remover(fotoLivro.getNomeArquivo());
		Mockito.verifyNoMoreInteractions(fotoStorage);
	}
	
	
	@Test
	void Dado_um_livroId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_FotoLivroNaoEncontradoException() {
		Mockito.when(repository.findFotoById(Mockito.anyLong()))
		.thenAnswer(answer -> {
			Long livroIdPassado = answer.getArgument(0, Long.class);
			throw new FotoLivroNaoEncontradaException(livroIdPassado);
		});

		final FotoLivroNaoEncontradaException e = 
				assertThrows(FotoLivroNaoEncontradaException.class, () -> service.buscarOuFalhar(livroId));
		
		assertEquals(e.getMessage(), String.format("Não existe um cadastro de foto para o livro com código %d", livroId));
		Mockito.verify(repository, Mockito.times(1)).findFotoById(livroId);
		
		Mockito.verify(repository, Mockito.never()).delete(fotoLivro);
		Mockito.verify(repository, Mockito.never()).flush();
		
		Mockito.verifyNoMoreInteractions(repository);
		
		Mockito.verify(fotoStorage, Mockito.never()).remover(fotoLivro.getNomeArquivo());
	}
	
}
