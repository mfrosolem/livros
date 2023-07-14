package com.maira.livrosapi.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.repository.AutorRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {

	@InjectMocks
	AutorService service;

	@Mock
	AutorRepository repository;

	Autor autor;
	
	Long autorId;

	@BeforeEach
	public void init() {
		autor = Autor.builder().nome("Joaquim Maria").sobrenome("Machado de Assis")
				.nomeConhecido("Machado de Assis").sexo("M").build();
		
		autorId = 1L;
	}
	
	@Test
	void Dado_um_autorId_valido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_um_autor() {
		when(repository.findById(anyLong())).thenAnswer(answer -> {
			Long idAutorRetorno = answer.getArgument(0, Long.class);
			return Optional.of(Autor.builder().
					id(idAutorRetorno).nome("Elena").sobrenome("Ferrante").
					nomeConhecido("Elena Ferrante").sexo("F").build());
		});
		
		Autor autorRetornado = service.buscarOuFalhar(autorId);
		
		assertInstanceOf(Autor.class, autorRetornado);
		assertEquals(autorId, autorRetornado.getId());
		verify(repository, Mockito.times(1)).findById(autorId);
	}
	
	@Test
	void Dado_um_autorId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_AutorNaoEncontradoException() {
		when(repository.findById(anyLong()))
			.thenThrow(new AutorNaoEncontradoException(autorId));
		
		assertThrows(AutorNaoEncontradoException.class, () -> service.buscarOuFalhar(autorId));
		verify(repository, Mockito.times(1)).findById(autorId);
	}
	
	@Test
	void Dado_um_autor_valido_Quando_salvar_Entao_deve_retornar_um_autor_com_id() {
		when(repository.save(any(Autor.class))).thenAnswer(answer -> {
			Autor autorPassado = answer.getArgument(0, Autor.class);
			autorPassado.setId(1L);
			return autorPassado;
		});
		
		Autor autorSalvo = service.salvar(autor);
		
		assertInstanceOf(Autor.class, autorSalvo);
		assertEquals(1L, autorSalvo.getId());
	}


	@Test
	void Dado_um_autor_valido_Quando_salvar_Entao_deve_chamar_metodo_save_do_repository() {
		when(repository.save(any(Autor.class)))
			.thenAnswer(answer -> {
				Autor autorPassado = answer.getArgument(0, Autor.class);
				autorPassado.setId(1L);
				return autorPassado;
			});
		
		service.salvar(autor);
		
		verify(repository, Mockito.times(1)).save(autor);
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_um_autor_valido_Quando_salvar_Entao_deve_lancar_exception_EntidadeEmUsoException_se_autor_ja_existe() {
		when(repository.save(any(Autor.class)))
		.thenThrow(new EntidadeEmUsoException(
				String.format("Autor(a) de nome %s %s já cadastrado(a)", autor.getNome(), autor.getSobrenome())));
		
		assertThrows(EntidadeEmUsoException.class, () -> service.salvar(autor));
	}
	
	@Test
	void Dado_um_autorId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_autor() {
		Mockito.doNothing().when(repository).deleteById(anyLong());
		Mockito.doNothing().when(repository).flush();
		
		service.excluir(autorId);
		
		verify(repository, Mockito.times(1)).deleteById(autorId);
		verify(repository, Mockito.times(1)).flush();
	}
	
	@Test
	void Dado_um_autorId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_AutorNaoEncontradoException() {
		Mockito.doThrow(new AutorNaoEncontradoException(autorId))
			.when(repository).deleteById(autorId);
		
		assertThrows(AutorNaoEncontradoException.class, () -> service.excluir(autorId));
		verify(repository, Mockito.times(1)).deleteById(autorId);
		verify(repository, Mockito.never()).flush();
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_um_autorId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		Mockito.doThrow(new EntidadeEmUsoException(
				String.format("Autor(a) de código %d não pode ser removido(a), pois está em uso", autorId)))
		.when(repository).deleteById(anyLong());
		
		assertThrows(EntidadeEmUsoException.class, () -> service.excluir(autorId));
		verify(repository, Mockito.times(1)).deleteById(autorId);
		verify(repository, Mockito.never()).flush();
		verifyNoMoreInteractions(repository);
	}
	 
	 
}
