package com.maira.livrosapi.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GeneroNaoEncontradoException;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.repository.GeneroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class GeneroServiceTest {

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
	@DisplayName("Dado um generoId valido Quando chamar metodo buscarOuFalhar Entao deve retornar um genero com id")
	void Dado_um_generoId_valido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_um_genero_com_id() {
		when(repository.findById(generoId)).thenAnswer(invocacao -> {
			Long generoIdPassado = invocacao.getArgument(0, Long.class);
			genero.setId(generoIdPassado);
			return Optional.of(genero);			
		});
		
		Genero sut = service.buscarOuFalhar(generoId);

		assertThat(sut).isInstanceOf(Genero.class);
		assertThat(sut.getId()).isNotNull();
		verify(repository, times(1)).findById(generoId);
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("Dado um generoId invalido Quando chamar metodo buscarOuFalhar Entao deve lancar exception GeneroNaoEncontradoException")
	void Dado_um_generoId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_GeneroNaoEncontradoException() {
		when(repository.findById(generoId))
				.thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.buscarOuFalhar(generoId))
				.isInstanceOf(GeneroNaoEncontradoException.class)
				.hasMessage(String.format("Não existe um cadastro de genero com o código %d", generoId));
		verify(repository, times(1)).findById(generoId);
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("Dado um genero valido Quando salvar Entao deve retornar um genero com id")
	void Dado_um_genero_valido_Quando_salvar_Entao_deve_retornar_um_genero_com_id() {
		when(repository.save(any(Genero.class))).thenAnswer(invocacao -> {
			Genero generoPassado = invocacao.getArgument(0, Genero.class);
			generoPassado.setId(generoId);
			return generoPassado;
		});

		Genero sut = service.salvar(genero);

		assertThat(sut).isInstanceOf(Genero.class);
		assertThat(sut.getId()).isNotNull();
	}

	@Test
	@DisplayName("Dado um genero valido Quando salvar Entao deve chamar metodo save do repository")
	void Dado_um_genero_valido_Quando_salvar_Entao_deve_chamar_metodo_save_do_repository() {
		when(repository.save(any(Genero.class))).thenAnswer(invocacao -> {
			Genero generoPassado = invocacao.getArgument(0, Genero.class);
			generoPassado.setId(generoId);
			return generoPassado;
		});

		service.salvar(genero);

		verify(repository, times(1)).save(any(Genero.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("Dado um genero que ja existe Quando salvar Entao deve lancar exception EntidadeEmUsoException")
	void Dado_um_genero_que_ja_existe_Quando_salvar_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		when(service.salvar(genero)).thenThrow(DataIntegrityViolationException.class);
		
		assertThatThrownBy(() -> service.salvar(genero)).isInstanceOf(EntidadeEmUsoException.class);
		verify(repository, times(1)).save(any(Genero.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("Dado um generoId valido Quando chamar metodo excluir Entao deve excluir genero")
	void Dado_um_generoId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_genero() {
		assertThatCode(() -> service.excluir(generoId)).doesNotThrowAnyException();
		verify(repository, times(1)).deleteById(anyLong());
		verify(repository, times(1)).flush();
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("Dado um generoId invalido Quando chamar metodo excluir Entao deve lancar exception GeneroNaoEncontradoException")
	void Dado_um_generoId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_GeneroNaoEncontradoException() {
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());

		assertThatThrownBy(() -> service.excluir(generoId)).isInstanceOf(GeneroNaoEncontradoException.class);
		verify(repository, times(1)).deleteById(anyLong());
		verify(repository, never()).flush();
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	@DisplayName("Dado um generoId em uso Quando chamar metodo excluir Entao deve lancar exception EntidadeEmUsoException")
	void Dado_um_generoId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());
		
		assertThatThrownBy(() -> service.excluir(generoId)).isInstanceOf(EntidadeEmUsoException.class);
		verify(repository, times(1)).deleteById(anyLong());
		verify(repository, never()).flush();
		verifyNoMoreInteractions(repository);
	}  

}
