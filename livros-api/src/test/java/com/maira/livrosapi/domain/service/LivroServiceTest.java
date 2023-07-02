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
		
		Mockito.when(autorService.buscarOuFalhar(Mockito.anyLong()))
		.thenAnswer(answer -> {
			Long autorIdPassado = answer.getArgument(0, Long.class);
			autor.setId(autorIdPassado);
			return autor;
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
		Mockito.verify(repository, Mockito.times(1)).save(livro);
		Mockito.verifyNoMoreInteractions(repository);
	}

}
