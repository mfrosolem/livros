package com.maira.livrosapi.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import org.springframework.mock.web.MockMultipartFile;

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
	
	MockMultipartFile fotoFile;
	
	
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
		
		fotoLivro = FotoLivro.builder()
				.id(livroId)
				.nomeArquivo("teste")
				.descricao("Capa_"+livroId)
				.contentType("image/jpeg")
				.tamanho(678L)
				.livro(livro)
				.build();
		
		fotoFile = new MockMultipartFile("foto", "foto", "image/jpeg", "foto".getBytes());
	}
	
	
	@Test
	void Dado_um_livroId_valido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_um_fotoLivro_com_id() {
		when(repository.findFotoById(anyLong()))
			.thenAnswer(answer -> {
				Long livroIdPassado = answer.getArgument(0, Long.class);
				fotoLivro.setId(livroIdPassado);
				return Optional.of(fotoLivro);
			});
		
		FotoLivro fotoLivroRetornado = service.buscarOuFalhar(livroId);
		
		assertInstanceOf(FotoLivro.class, fotoLivroRetornado);
		assertEquals(livroId, fotoLivroRetornado.getId());
		verify(repository, Mockito.times(1)).findFotoById(livroId);
		verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	void Dado_um_livroId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_FotoLivroNaoEncontradoException() {
		when(repository.findFotoById(anyLong()))
			.thenAnswer(answer -> {
				Long livroIdPassado = answer.getArgument(0, Long.class);
				throw new FotoLivroNaoEncontradaException(livroIdPassado);
			});
		
		final FotoLivroNaoEncontradaException e = 
				assertThrows(FotoLivroNaoEncontradaException.class, () -> service.buscarOuFalhar(livroId));
		
		assertEquals(e.getMessage(), String.format("Não existe um cadastro de foto para o livro com código %d", livroId));
		verify(repository, Mockito.times(1)).findFotoById(livroId);
		verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	void Dado_um_livroId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_fotoLivro () {
		when(repository.findFotoById(anyLong()))
		.thenAnswer(answer -> {
			Long livroIdPassado = answer.getArgument(0, Long.class);
			fotoLivro.setId(livroIdPassado);
			return Optional.of(fotoLivro);
		});
		Mockito.doNothing().when(repository).delete(Mockito.any(FotoLivro.class));
		Mockito.doNothing().when(repository).flush();
		Mockito.doNothing().when(fotoStorage).remover(fotoLivro.getNomeArquivo());
		
		service.excluir(livroId);
		
		verify(repository, Mockito.times(1)).delete(fotoLivro);
		verify(repository, Mockito.times(1)).flush();
		verifyNoMoreInteractions(repository);
		
		verify(fotoStorage, Mockito.times(1)).remover(fotoLivro.getNomeArquivo());
		verifyNoMoreInteractions(fotoStorage);
	}
	
	
	@Test
	void Dado_um_livroId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_FotoLivroNaoEncontradoException() {
		when(repository.findFotoById(anyLong()))
		.thenAnswer(answer -> {
			Long livroIdPassado = answer.getArgument(0, Long.class);
			throw new FotoLivroNaoEncontradaException(livroIdPassado);
		});

		final FotoLivroNaoEncontradaException e = 
				assertThrows(FotoLivroNaoEncontradaException.class, () -> service.buscarOuFalhar(livroId));
		
		assertEquals(e.getMessage(), String.format("Não existe um cadastro de foto para o livro com código %d", livroId));
		verify(repository, Mockito.times(1)).findFotoById(livroId);
		
		verify(repository, Mockito.never()).delete(fotoLivro);
		verify(repository, Mockito.never()).flush();
		
		verifyNoMoreInteractions(repository);
		
		verify(fotoStorage, Mockito.never()).remover(fotoLivro.getNomeArquivo());
	}
	
	
	@Test
	void Dado_um_fotoLivro_e_inputStream_validos_Quando_salvar_Entao_deve_retornar_um_fotoLivro() throws IOException {
		
		when(repository.findFotoById(anyLong()))
		.thenAnswer(answer -> {
			return Optional.empty();
		});
		
		when(repository.save(Mockito.any(FotoLivro.class)))
		.thenAnswer(answer -> {
			return answer.getArgument(0, FotoLivro.class);
		});
		
		FotoLivro fotoLivroSalvo = service.salvar(fotoLivro, fotoFile.getInputStream());
		
		assertInstanceOf(FotoLivro.class, fotoLivroSalvo);
		assertEquals(fotoLivro.getId(), fotoLivroSalvo.getId());
		
		verify(repository, Mockito.never()).delete(fotoLivro);
		verify(repository, Mockito.times(1)).save(fotoLivro);
		verify(repository, Mockito.times(1)).flush();
		verifyNoMoreInteractions(repository);
	}
	
}
