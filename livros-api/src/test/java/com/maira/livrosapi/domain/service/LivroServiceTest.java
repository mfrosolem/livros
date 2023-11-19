package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.*;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.model.Livro;
import com.maira.livrosapi.domain.repository.LivroRepository;
import com.maira.livrosapi.domain.service.impl.LivroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LivroServiceTest {
	
	@InjectMocks
	LivroServiceImpl service;
	
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
		
		Livro sut = service.buscarOuFalhar(livroId);

		assertThat(sut).isInstanceOf(Livro.class);
		assertThat(sut.getId()).isNotNull();
		verify(repository, times(1)).findById(anyLong());
		verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	@DisplayName("Dado um livroId invalido Quando chamar metodo buscarOuFalhar Entao deve lancar exception LivroNaoEncontradoException")
	void Dado_um_livroId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_LivroNaoEncontradoException() {
		when(repository.findById(anyLong()))
			.thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> service.buscarOuFalhar(livroId))
				.isInstanceOf(LivroNaoEncontradoException.class)
				.hasMessage(String.format("Não existe um cadastro de livro com o código %d", livroId));
		verify(repository, times(1)).findById(anyLong());
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	@DisplayName("Dado um livro valido Quando salvar Entao deve retornar um livro com id")
	void Dado_um_livro_valido_Quando_salvar_Entao_deve_retornar_um_livro_com_id() {
		
		this.buscarAutorComSucesso();
		this.buscarGeneroComSucesso();
		this.buscarEditoraComSucesso();

		when(repository.save(any(Livro.class)))
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
		
		Livro sut = service.salvar(livro);

		assertThat(sut).isInstanceOf(Livro.class);
		assertThat(sut.getId()).isNotNull();
		verify(autorService, times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(autorService);
		verify(generoService, times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(generoService);
		verify(editoraService, times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(editoraService);
		verify(repository, times(1)).save(any(Livro.class));
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

		assertThatThrownBy(() -> service.salvar(livro))
				.isInstanceOf(AutorNaoEncontradoException.class)
				.hasMessage(String.format("Não existe um cadastro de autor(a) com o código %d", autorId));
		verify(autorService, times(1)).buscarOuFalhar(anyLong());
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


		assertThatThrownBy(() -> service.salvar(livro))
				.isInstanceOf(GeneroNaoEncontradoException.class)
				.hasMessage(String.format("Não existe um cadastro de genero com o código %d", generoId));
		verify(autorService, times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(autorService);
		verify(generoService, times(1)).buscarOuFalhar(anyLong());
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

		assertThatThrownBy(() -> service.salvar(livro))
				.isInstanceOf(EditoraNaoEncontradaException.class)
				.hasMessage(String.format("Não existe um cadastro de editora com o código %d", editoraId));

		verify(autorService, times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(autorService);

		verify(generoService, times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(generoService);

		verify(editoraService, times(1)).buscarOuFalhar(anyLong());

		verifyNoInteractions(repository);
	}
	
	
	@Test
	@DisplayName("Dado um livro com ISBN ja cadastrado Quando salvar Entao deve retornar exception EntidadeEmUsoException")
	void Dado_um_livro_com_ISBN_ja_cadastrado_Quando_salvar_Entao_deve_retornar_exception_EntidadeEmUsoException() {
		this.buscarAutorComSucesso();
		this.buscarGeneroComSucesso();
		this.buscarEditoraComSucesso();
		
		when(repository.save(any(Livro.class))).thenThrow(DataIntegrityViolationException.class);
		
		genero.setId(generoId);
		editora.setId(editoraId);
		autor.setId(autorId);
		livro.setGenero(genero);
		livro.setEditora(editora);
		livro.setAutor(autor);

		assertThatThrownBy(() -> service.salvar(livro))
				.isInstanceOf(EntidadeEmUsoException.class)
				.hasMessage(String.format("Livro de título %s já cadastrado", livro.getTitulo()));

		verify(autorService, times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(autorService);

		verify(generoService, times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(generoService);

		verify(editoraService, times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(editoraService);

		verify(repository, times(1)).save(any(Livro.class));
		verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	@DisplayName("Dado um livroId valido Quando chamar metodo excluir Entao deve excluir livro")
	void Dado_um_livroId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_livro () {
		assertThatCode(() -> service.excluir(livroId)).doesNotThrowAnyException();
		verify(repository, times(1)).deleteById(anyLong());
		verify(repository, times(1)).flush();
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	@DisplayName("Dado um livroId invalido Quando chamar metodo excluir Entao deve lancar exception LivroNaoEncontradoException")
	void Dado_um_livroId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_LivroNaoEncontradoException() {
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());

		assertThatThrownBy(()-> service.excluir(livroId))
				.isInstanceOf(LivroNaoEncontradoException.class)
				.hasMessage(String.format("Não existe um cadastro de livro com o código %d", livroId));
		verify(repository, times(1)).deleteById(anyLong());
		verify(repository, never()).flush();
		verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	@DisplayName("Dado um livroId em uso Quando chamar metodo excluir Entao deve lancar exception EntidadeEmUsoException")
	void Dado_um_livroId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());
		
		assertThatThrownBy(() -> service.excluir(livroId)).isInstanceOf(EntidadeEmUsoException.class);
		verify(repository, times(1)).deleteById(anyLong());
		verify(repository, never()).flush();
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("Quando chamar metodo listByTituloContaining Entao deve retornar todos os livros")
	void listByTituloContaining_RetornaTodosAutores() {

		var livroUm = Livro.builder().id(1L).isbn("9788582850350").titulo("Dom Casmurro").idioma("Português")
				.paginas(390L).ano(2016L).build();

		var livroDois = Livro.builder().id(2L).isbn("9788582850350").titulo("Dom Casmurro").idioma("Português")
				.paginas(390L).ano(2016L).build();

		var livroTres = Livro.builder().id(3L).isbn("9788582850350").titulo("Dom Casmurro").idioma("Português")
				.paginas(390L).ano(2016L).build();
		List<Livro> livros = new ArrayList<>() {
			{
				add(livroUm);
				add(livroDois);
				add(livroTres);
			}
		};
		Pageable pageable = PageRequest.of(0,20);
		Page<Livro> page = new PageImpl<>(livros,pageable, livros.size());
		when(repository.findByTituloContaining(anyString(), any(Pageable.class))).thenReturn(page);

		Page<Livro> sut = service.listByContaining("", pageable);

		assertThat(sut).isNotEmpty().hasSize(3);
	}

	@Test
	@DisplayName("Quando chamar metodo listByTituloContaining filtrando por titulo inexistente Entao deve retornar lista vazia")
	void listByTituloContaining_Filtrando_RetornaListaVazia() {
		List<Livro> livros = new ArrayList<>();
		Pageable pageable = PageRequest.of(0,20);
		Page<Livro> page = new PageImpl<>(livros,pageable, livros.size());
		when(repository.findByTituloContaining(anyString(), any(Pageable.class))).thenReturn(page);

		Page<Livro> sut = service.listByContaining("A Sangue Frio", pageable);
		assertThat(sut).isEmpty();
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
