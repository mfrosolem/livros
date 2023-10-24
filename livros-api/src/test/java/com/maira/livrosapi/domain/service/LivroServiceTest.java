package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.*;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.model.Livro;
import com.maira.livrosapi.domain.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LivroServiceTest {
	
	@InjectMocks
	LivroService service;
	
	@Mock
	LivroRepository repository;
	
	@Mock
	AutorService autorService;
	
	@Mock
	GeneroService generoService;
	
	@Mock
	EditoraService editoraService;
	
	Livro livro;
	Long livroId;
	Autor autor;
	Long autorId;
	Genero genero;
	Long generoId;
	Editora editora;
	Long editoraId;
	
	@BeforeEach
	void init() {
		
		genero = Genero.builder().descricao("Romance").build();
		generoId = 1L;
		
		editora = Editora.builder().nome("Penguin & Companhia das Letras").build();
		editoraId = 1L;
		
		autor = Autor.builder().nome("Joaquim Maria").sobrenome("Machado de Assis")
				.nomeConhecido("Machado de Assis").sexo("M").build();
		autorId = 1L;
		
		livro = Livro.builder().isbn("9788582850350").titulo("Dom Casmurro").idioma("Português")
				.paginas(390L).ano(2016L).build();
		livroId = 1L;
	}
	
	
	@Test
	@DisplayName("Dado um livroId valido Quando chamar metodo buscarOuFalhar Entao deve retornar um livro com id")
	void Dado_um_livroId_valido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_um_livro_com_id() {
		when(repository.findById(anyLong()))
		.thenAnswer(answer -> {
			Long livroIdPassado = answer.getArgument(0, Long.class);
			livro.setId(livroIdPassado);
			return Optional.of(livro);
		});
		
		Livro livroRetornado = service.buscarOuFalhar(livroId);
		
		assertInstanceOf(Livro.class, livroRetornado);
		assertEquals(livroId, livroRetornado.getId());
		verify(repository, Mockito.times(1)).findById(anyLong());
		verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	@DisplayName("Dado um livroId invalido Quando chamar metodo buscarOuFalhar Entao deve lancar exception LivroNaoEncontradoException")
	void Dado_um_livroId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_LivroNaoEncontradoException() {
		when(repository.findById(anyLong()))
			.thenThrow(new LivroNaoEncontradoException(livroId));
		
		final LivroNaoEncontradoException e = assertThrows(LivroNaoEncontradoException.class, () -> {
			service.buscarOuFalhar(livroId);
        });
		
		assertEquals(String.format("Não existe um cadastro de livro com o código %d", livroId), e.getMessage());
		verify(repository, Mockito.times(1)).findById(anyLong());
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	@DisplayName("Dado um livro valido Quando salvar Entao deve retornar um livro com id")
	void Dado_um_livro_valido_Quando_salvar_Entao_deve_retornar_um_livro_com_id() {
		
		this.buscarAutorComSucesso();
		this.buscarGeneroComSucesso();
		this.buscarEditoraComSucesso();

		when(repository.save(Mockito.any(Livro.class)))
			.thenAnswer(answer -> {
				Livro livroPassado = answer.getArgument(0, Livro.class);
				livroPassado.setId(livroId);
				return livroPassado;
			});
		
		genero.setId(generoId);
		editora.setId(editoraId);
		autor.setId(autorId);
		livro.setGenero(genero);
		livro.setEditora(editora);
		livro.setAutor(autor);
		
		Livro livroSalvo = service.salvar(livro);
		
		assertInstanceOf(Livro.class, livroSalvo);
		assertEquals(livroId, livroSalvo.getId());
		
		verify(autorService, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(autorService);
		
		verify(generoService, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(generoService);
		
		verify(editoraService, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(editoraService);
		
		verify(repository, Mockito.times(1)).save(Mockito.any(Livro.class));
		verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	@DisplayName("Dado um livro com autorId invalido Quando salvar Entao deve retornar exception AutorNaoEncontradoException")
	void Dado_um_livro_com_autorId_invalido_Quando_salvar_Entao_deve_retornar_exception_AutorNaoEncontradoException() {
		when(autorService.buscarOuFalhar(anyLong()))
		.thenAnswer(answer -> {
			Long autorIdPassado = answer.getArgument(0, Long.class);
			throw new AutorNaoEncontradoException(autorIdPassado);
		});

		autor.setId(autorId);
		genero.setId(generoId);
		editora.setId(editoraId);
		livro.setAutor(autor);
		livro.setGenero(genero);
		livro.setEditora(editora);
		
		final AutorNaoEncontradoException e = 
				assertThrows(AutorNaoEncontradoException.class, () -> service.salvar(livro));
		
		assertEquals(String.format("Não existe um cadastro de autor(a) com o código %d", autorId), e.getMessage());
		verify(autorService, Mockito.times(1)).buscarOuFalhar(anyLong());
		
		verifyNoInteractions(generoService);
		verifyNoInteractions(editoraService);
		verifyNoInteractions(repository);
	}
	

	@Test
	@DisplayName("Dado um livro com generoId invalido Quando salvar Entao deve retornar exception GeneroNaoEncontradoException")
	void Dado_um_livro_com_generoId_invalido_Quando_salvar_Entao_deve_retornar_exception_GeneroNaoEncontradoException () {
		this.buscarAutorComSucesso();
		
		when(generoService.buscarOuFalhar(anyLong()))
		.thenAnswer(answer -> {
			Long generoIdPassado = answer.getArgument(0, Long.class);
			throw new GeneroNaoEncontradoException(generoIdPassado);
		});
		
		autor.setId(autorId);
		genero.setId(generoId);
		editora.setId(editoraId);
		livro.setAutor(autor);
		livro.setGenero(genero);
		livro.setEditora(editora);

		final GeneroNaoEncontradoException e = 
				assertThrows(GeneroNaoEncontradoException.class, () -> service.salvar(livro));
		
		verify(autorService, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(autorService);

		assertEquals(String.format("Não existe um cadastro de genero com o código %d", generoId), e.getMessage());
		verify(generoService, Mockito.times(1)).buscarOuFalhar(anyLong());
	
		verifyNoInteractions(editoraService);
		verifyNoInteractions(repository);
	}
	
	
	@Test
	@DisplayName("Dado um livro com editoraId invalida Quando salvar Entao deve retornar exception EditoraNaoEncontradaException")
	void Dado_um_livro_com_editoraId_invalida_Quando_salvar_Entao_deve_retornar_exception_EditoraNaoEncontradaException () {
		this.buscarAutorComSucesso();
		this.buscarGeneroComSucesso();
		
		when(editoraService.buscarOuFalhar(anyLong()))
		.thenAnswer(answer -> {
			Long editoraIdPassada = answer.getArgument(0, Long.class);
			throw new EditoraNaoEncontradaException(editoraIdPassada);
		});
	
	
		autor.setId(autorId);
		genero.setId(generoId);
		editora.setId(editoraId);
		livro.setAutor(autor);
		livro.setGenero(genero);
		livro.setEditora(editora);

		final EditoraNaoEncontradaException e = 
				assertThrows(EditoraNaoEncontradaException.class, () -> service.salvar(livro));

		verify(autorService, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(autorService);

		verify(generoService, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(generoService);
		
		assertEquals(String.format("Não existe um cadastro de editora com o código %d", editoraId), e.getMessage());
		verify(editoraService, Mockito.times(1)).buscarOuFalhar(anyLong());
		
		verifyNoInteractions(repository);
	}
	
	
	@Test
	@DisplayName("Dado um livro com ISBN ja cadastrado Quando salvar Entao deve retornar exception EntidadeEmUsoException")
	void Dado_um_livro_com_ISBN_ja_cadastrado_Quando_salvar_Entao_deve_retornar_exception_EntidadeEmUsoException() {
		this.buscarAutorComSucesso();
		this.buscarGeneroComSucesso();
		this.buscarEditoraComSucesso();
		
		when(repository.save(Mockito.any(Livro.class))).thenThrow(DataIntegrityViolationException.class);
		
		genero.setId(generoId);
		editora.setId(editoraId);
		autor.setId(autorId);
		livro.setGenero(genero);
		livro.setEditora(editora);
		livro.setAutor(autor);
		
		final EntidadeEmUsoException e = 
				assertThrows(EntidadeEmUsoException.class, () -> service.salvar(livro));
		
		assertEquals(String.format("Livro de título %s já cadastrado", livro.getTitulo()), e.getMessage());
		
		verify(autorService, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(autorService);

		verify(generoService, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(generoService);
		
		verify(editoraService, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(editoraService);
		
		verify(repository, Mockito.times(1)).save(Mockito.any(Livro.class));
		verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	@DisplayName("Dado um livroId valido Quando chamar metodo excluir Entao deve excluir livro")
	void Dado_um_livroId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_livro () {
		service.excluir(livroId);

		verify(repository, Mockito.times(1)).deleteById(anyLong());
		verify(repository, Mockito.times(1)).flush();
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	@DisplayName("Dado um livroId invalido Quando chamar metodo excluir Entao deve lancar exception LivroNaoEncontradoException")
	void Dado_um_livroId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_LivroNaoEncontradoException() {
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());
		
		final LivroNaoEncontradoException e = 
				assertThrows(LivroNaoEncontradoException.class, ()-> service.excluir(livroId));
		
		assertEquals(String.format("Não existe um cadastro de livro com o código %d", livroId), e.getMessage());
		verify(repository, Mockito.times(1)).deleteById(anyLong());
		verify(repository, Mockito.never()).flush();
		verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	@DisplayName("Dado um livroId em uso Quando chamar metodo excluir Entao deve lancar exception EntidadeEmUsoException")
	void Dado_um_livroId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());
		
		assertThrows(EntidadeEmUsoException.class, () -> service.excluir(livroId));
		verify(repository, Mockito.times(1)).deleteById(anyLong());
		verify(repository, Mockito.never()).flush();
		verifyNoMoreInteractions(repository);
	}

	private void buscarAutorComSucesso() {
		when(autorService.buscarOuFalhar(anyLong()))
				.thenAnswer(answer -> {
					Long autorIdPassado = answer.getArgument(0, Long.class);
					autor.setId(autorIdPassado);
					return autor;
				});
	}

	private void buscarGeneroComSucesso() {
		when(generoService.buscarOuFalhar(anyLong()))
				.thenAnswer(answer -> {
					Long generoIdPassado = answer.getArgument(0, Long.class);
					genero.setId(generoIdPassado);
					return genero;
				});
	}

	private void buscarEditoraComSucesso() {
		when(editoraService.buscarOuFalhar(anyLong()))
				.thenAnswer(answer -> {
					Long editoraIdPassada = answer.getArgument(0, Long.class);
					editora.setId(editoraIdPassada);
					return editora;
				});
	}
	 
}
