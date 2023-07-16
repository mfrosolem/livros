package com.maira.livrosapi.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GeneroNaoEncontradoException;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.repository.GeneroRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class GeneroServiceTest {

	@InjectMocks
	GeneroService service;

	@Mock
	GeneroRepository repository;

	Genero genero;
	
	Long generoId;

	@BeforeEach
	void init() {
		genero = Genero.builder().descricao("Romance").build();
		generoId = 1L;
	}
	
	@Test
	void Dado_um_generoId_valido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_um_genero_com_id() {
		when(repository.findById(generoId)).thenAnswer(invocacao -> {
			Long generoIdPassado = invocacao.getArgument(0, Long.class);
			genero.setId(generoIdPassado);
			return Optional.of(genero);			
		});
		
		Genero generoRetornado = service.buscarOuFalhar(generoId);
		
		assertInstanceOf(Genero.class, generoRetornado);
		assertEquals(generoId, generoRetornado.getId());
		verify(repository, Mockito.times(1)).findById(generoId);
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_um_generoId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_GeneroNaoEncontradoException() {
		when(repository.findById(generoId))
			.thenThrow(new GeneroNaoEncontradoException(generoId));
		
		assertThrows(GeneroNaoEncontradoException.class, () -> service.buscarOuFalhar(generoId));
		verify(repository, Mockito.times(1)).findById(generoId);
		verifyNoMoreInteractions(repository);
	}

	@Test
	void Dado_um_genero_valido_Quando_salvar_Entao_deve_retornar_um_genero_com_id() {
		when(repository.save(Mockito.any(Genero.class))).thenAnswer(invocacao -> {
			Genero generoPassado = invocacao.getArgument(0, Genero.class);
			generoPassado.setId(generoId);
			return generoPassado;
		});

		Genero generoSalvo = service.salvar(genero);

		assertInstanceOf(Genero.class, generoSalvo);
		assertEquals(generoId, generoSalvo.getId());
	}

	@Test
	void Dado_um_genero_valido_Quando_salvar_Entao_deve_chamar_metodo_save_do_repository() {
		when(repository.save(Mockito.any(Genero.class))).thenAnswer(invocacao -> {
			Genero generoPassado = invocacao.getArgument(0, Genero.class);
			generoPassado.setId(generoId);
			return generoPassado;
		});

		service.salvar(genero);

		verify(repository, Mockito.times(1)).save(Mockito.eq(genero));
		verifyNoMoreInteractions(repository);
	}

	@Test
	void Dado_um_genero_que_ja_existe_Quando_salvar_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		when(service.salvar(genero)).thenThrow(DataIntegrityViolationException.class);
		
		assertThrows(EntidadeEmUsoException.class, () -> service.salvar(genero));
		verify(repository, Mockito.times(1)).save(Mockito.any(Genero.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	void Dado_um_generoId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_genero() {
		Mockito.doNothing().when(repository).deleteById(Mockito.anyLong());
		Mockito.doNothing().when(repository).flush();
		
		service.excluir(generoId);

		verify(repository, Mockito.times(1)).deleteById(anyLong());
		verify(repository, Mockito.times(1)).flush();
		verifyNoMoreInteractions(repository);
	}

	@Test
	void Dado_um_generoId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_GeneroNaoEncontradoException() {
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());

		assertThrows(GeneroNaoEncontradoException.class, () -> service.excluir(generoId));
		verify(repository, Mockito.times(1)).deleteById(anyLong());
		verify(repository, Mockito.never()).flush();
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_um_generoId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());
		
		assertThrows(EntidadeEmUsoException.class, () -> service.excluir(generoId));
		verify(repository, Mockito.times(1)).deleteById(anyLong());
		verify(repository, Mockito.never()).flush();
		verifyNoMoreInteractions(repository);
	}  

}
