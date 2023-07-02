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

import com.maira.livrosapi.domain.exception.AutorNaoEncontradoException;
import com.maira.livrosapi.domain.exception.EditoraNaoEncontradaException;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GeneroNaoEncontradoException;
import com.maira.livrosapi.domain.exception.LivroNaoEncontradoException;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.model.Livro;
import com.maira.livrosapi.domain.repository.LivroRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {
	
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
	void Dado_um_livroId_valido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_um_livro_com_id() {
		Mockito.when(repository.findById(Mockito.anyLong()))
		.thenAnswer(answer -> {
			Long livroIdPassado = answer.getArgument(0, Long.class);
			livro.setId(livroIdPassado);
			return Optional.of(livro);
		});
		
		Livro livroRetornado = service.buscarOuFalhar(livroId);
		
		assertInstanceOf(Livro.class, livroRetornado);
		assertEquals(livroId, livroRetornado.getId());
		Mockito.verify(repository, Mockito.times(1)).findById(livroId);
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	void Dado_um_livroId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_LivroNaoEncontradoException() {
		Mockito.when(repository.findById(Mockito.anyLong()))
			.thenThrow(new LivroNaoEncontradoException(livroId));
		
		final LivroNaoEncontradoException e = assertThrows(LivroNaoEncontradoException.class, () -> {
			service.buscarOuFalhar(livroId);
        });
		
		assertEquals(e.getMessage(), String.format("Não existe um cadastro de livro com o código %d", livroId));
		Mockito.verify(repository, Mockito.times(1)).findById(livroId);
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_um_livro_valido_Quando_salvar_Entao_deve_retornar_um_livro_com_id() {
		
		Mockito.when(autorService.buscarOuFalhar(Mockito.anyLong()))
		.thenAnswer(answer -> {
			Long autorIdPassado = answer.getArgument(0, Long.class);
			autor.setId(autorIdPassado);
			return autor;
		});
		
		Mockito.when(generoService.buscarOuFalhar(Mockito.anyLong()))
			.thenAnswer(answer -> {
				Long generoIdPassado = answer.getArgument(0, Long.class);
				genero.setId(generoIdPassado);
				return genero;
			});
		
		Mockito.when(editoraService.buscarOuFalhar(Mockito.anyLong()))
		.thenAnswer(answer -> {
			Long editoraIdPassada = answer.getArgument(0, Long.class);
			editora.setId(editoraIdPassada);
			return editora;
		});
		
		Mockito.when(repository.save(Mockito.any(Livro.class)))
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
		
		Mockito.verify(autorService, Mockito.times(1)).buscarOuFalhar(autorId);
		Mockito.verifyNoMoreInteractions(autorService);
		
		Mockito.verify(generoService, Mockito.times(1)).buscarOuFalhar(generoId);
		Mockito.verifyNoMoreInteractions(generoService);
		
		Mockito.verify(editoraService, Mockito.times(1)).buscarOuFalhar(editoraId);
		Mockito.verifyNoMoreInteractions(editoraService);
		
		Mockito.verify(repository, Mockito.times(1)).save(livro);
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	void Dado_um_livro_com_autorId_invalido_Quando_salvar_Entao_deve_retornar_exception_AutorNaoEncontradoException() {

		Mockito.when(autorService.buscarOuFalhar(Mockito.anyLong()))
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
		
		assertEquals(e.getMessage(), String.format("Não existe um cadastro de autor(a) com o código %d", autorId));
		Mockito.verify(autorService, Mockito.times(1)).buscarOuFalhar(autorId);
		
		Mockito.verifyNoInteractions(generoService);
		Mockito.verifyNoInteractions(editoraService);
		Mockito.verifyNoInteractions(repository);
	}
	

	@Test
	void Dado_um_livro_com_generoId_invalido_Quando_salvar_Entao_deve_retornar_exception_GeneroNaoEncontradoException () { 
		
		Mockito.when(autorService.buscarOuFalhar(Mockito.anyLong()))
		.thenAnswer(answer -> {
			Long autorIdPassado = answer.getArgument(0, Long.class);
			autor.setId(autorIdPassado);
			return autor;
		});
		
		Mockito.when(generoService.buscarOuFalhar(Mockito.anyLong()))
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
		
		Mockito.verify(autorService, Mockito.times(1)).buscarOuFalhar(autorId);
		Mockito.verifyNoMoreInteractions(autorService);

		assertEquals(e.getMessage(), String.format("Não existe um cadastro de genero com o código %d", generoId));
		Mockito.verify(generoService, Mockito.times(1)).buscarOuFalhar(generoId);
	
		Mockito.verifyNoInteractions(editoraService);
		Mockito.verifyNoInteractions(repository);
	}
	
	
	@Test
	void Dado_um_livro_com_editoraId_invalida_Quando_salvar_Entao_deve_retornar_exception_EditoraNaoEncontradaException () {
		
		Mockito.when(autorService.buscarOuFalhar(Mockito.anyLong()))
		.thenAnswer(answer -> {
			Long autorIdPassado = answer.getArgument(0, Long.class);
			autor.setId(autorIdPassado);
			return autor;
		});
		
		Mockito.when(generoService.buscarOuFalhar(Mockito.anyLong()))
		.thenAnswer(answer -> {
			Long generoIdPassado = answer.getArgument(0, Long.class);
			genero.setId(generoIdPassado);
			return genero;
		});
		
		Mockito.when(editoraService.buscarOuFalhar(Mockito.anyLong()))
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

		Mockito.verify(autorService, Mockito.times(1)).buscarOuFalhar(autorId);
		Mockito.verifyNoMoreInteractions(autorService);

		Mockito.verify(generoService, Mockito.times(1)).buscarOuFalhar(autorId);
		Mockito.verifyNoMoreInteractions(generoService);
		
		assertEquals(e.getMessage(), String.format("Não existe um cadastro de editora com o código %d", editoraId));
		Mockito.verify(editoraService, Mockito.times(1)).buscarOuFalhar(editoraId);
		
		Mockito.verifyNoInteractions(repository);
	}
	
	
	@Test
	void Dado_um_livro_com_ISBN_ja_cadastrado_Quando_salvar_Entao_deve_retornar_exception_EntidadeEmUsoException() {
		
		Mockito.when(autorService.buscarOuFalhar(Mockito.anyLong()))
		.thenAnswer(answer -> {
			Long autorIdPassado = answer.getArgument(0, Long.class);
			autor.setId(autorIdPassado);
			return autor;
		});
		
		Mockito.when(generoService.buscarOuFalhar(Mockito.anyLong()))
			.thenAnswer(answer -> {
				Long generoIdPassado = answer.getArgument(0, Long.class);
				genero.setId(generoIdPassado);
				return genero;
			});
		
		Mockito.when(editoraService.buscarOuFalhar(Mockito.anyLong()))
		.thenAnswer(answer -> {
			Long editoraIdPassada = answer.getArgument(0, Long.class);
			editora.setId(editoraIdPassada);
			return editora;
		});
		
		Mockito.when(repository.save(Mockito.any(Livro.class)))
			.thenAnswer(answer -> {
				Livro livroPassado = answer.getArgument(0, Livro.class);
				throw new EntidadeEmUsoException(
						String.format("Livro de título %s já cadastrado", livroPassado.getTitulo()));
			});
		
		genero.setId(generoId);
		editora.setId(editoraId);
		autor.setId(autorId);
		livro.setGenero(genero);
		livro.setEditora(editora);
		livro.setAutor(autor);
		
		final EntidadeEmUsoException e = 
				assertThrows(EntidadeEmUsoException.class, () -> service.salvar(livro));
		
		assertEquals(e.getMessage(), String.format("Livro de título %s já cadastrado", livro.getTitulo()));
		
		Mockito.verify(autorService, Mockito.times(1)).buscarOuFalhar(autorId);
		Mockito.verifyNoMoreInteractions(autorService);

		Mockito.verify(generoService, Mockito.times(1)).buscarOuFalhar(autorId);
		Mockito.verifyNoMoreInteractions(generoService);
		
		Mockito.verify(editoraService, Mockito.times(1)).buscarOuFalhar(editoraId);
		Mockito.verifyNoMoreInteractions(editoraService);
		
		Mockito.verify(repository, Mockito.times(1)).save(livro);
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	void Dado_um_livroId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_livro () {
		service.excluir(livroId);
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(livroId);
		Mockito.verify(repository, Mockito.times(1)).flush();
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_um_livroId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_LivroNaoEncontradoException() {
		Mockito.doThrow(new LivroNaoEncontradoException(livroId))
			.when(repository).deleteById(livroId);
		
		final LivroNaoEncontradoException e = 
				assertThrows(LivroNaoEncontradoException.class, ()-> service.excluir(livroId));
		
		assertEquals(e.getMessage(), String.format("Não existe um cadastro de livro com o código %d", livroId));
		Mockito.verify(repository, Mockito.times(1)).deleteById(livroId);
		Mockito.verify(repository, Mockito.never()).flush();
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	void Dado_um_livroId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		Mockito.doThrow(new EntidadeEmUsoException(
				String.format("Editora de código %d não pode ser removida, pois está em uso", editoraId)))
			.when(repository).deleteById(livroId);
		
		assertThrows(EntidadeEmUsoException.class, () -> service.excluir(livroId));
		Mockito.verify(repository, Mockito.times(1)).deleteById(editoraId);
		Mockito.verify(repository, Mockito.never()).flush();
		Mockito.verifyNoMoreInteractions(repository);
	}
	 
}
