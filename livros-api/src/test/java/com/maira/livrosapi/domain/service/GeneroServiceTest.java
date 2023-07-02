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
		Mockito.when(repository.findById(generoId)).thenAnswer(invocacao -> {
			Long generoIdPassado = invocacao.getArgument(0, Long.class);
			genero.setId(generoIdPassado);
			return Optional.of(genero);			
		});
		
		Genero generoRetornado = service.buscarOuFalhar(generoId);
		
		assertInstanceOf(Genero.class, generoRetornado);
		assertEquals(generoId, generoRetornado.getId());
		Mockito.verify(repository, Mockito.times(1)).findById(generoId);
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_um_generoId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_GeneroNaoEncontradoException() {
		Mockito.when(repository.findById(generoId))
			.thenThrow(new GeneroNaoEncontradoException(generoId));
		
		assertThrows(GeneroNaoEncontradoException.class, () -> service.buscarOuFalhar(generoId));
		Mockito.verify(repository, Mockito.times(1)).findById(generoId);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	void Dado_um_genero_valido_Quando_salvar_Entao_deve_retornar_um_genero_com_id() {
		Mockito.when(repository.save(Mockito.any(Genero.class))).thenAnswer(invocacao -> {
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
		Mockito.when(repository.save(Mockito.any(Genero.class))).thenAnswer(invocacao -> {
			Genero generoPassado = invocacao.getArgument(0, Genero.class);
			generoPassado.setId(generoId);
			return generoPassado;
		});

		service.salvar(genero);

		Mockito.verify(repository, Mockito.times(1)).save(Mockito.eq(genero));
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	void Dado_um_genero_valido_Quando_salvar_Entao_deve_lancar_exception_EntidadeEmUsoException_se_genero_ja_existe() {
		Mockito.when(service.salvar(genero)).thenThrow(new EntidadeEmUsoException(
				String.format("Genero de descrição %s já cadastrado", genero.getDescricao())));
		
		assertThrows(EntidadeEmUsoException.class, () -> service.salvar(genero));
	}

	@Test
	void Dado_um_generoId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_genero() {
		service.excluir(generoId);

		Mockito.verify(repository, Mockito.times(1)).deleteById(generoId);
		Mockito.verify(repository, Mockito.times(1)).flush();
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	void Dado_um_generoId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_GeneroNaoEncontradoException() {
		Mockito.doThrow(new GeneroNaoEncontradoException(generoId))
			.when(repository).deleteById(generoId);

		assertThrows(GeneroNaoEncontradoException.class, () -> service.excluir(generoId));
		Mockito.verify(repository, Mockito.times(1)).deleteById(generoId);
		Mockito.verify(repository, Mockito.never()).flush();
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_um_generoId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		Mockito.doThrow(new EntidadeEmUsoException(
				String.format("Genero de código %d não pode ser removido, pois está em uso", generoId)))
			.when(repository).deleteById(generoId);
		
		assertThrows(EntidadeEmUsoException.class, () -> service.excluir(generoId));
		Mockito.verify(repository, Mockito.times(1)).deleteById(generoId);
		Mockito.verify(repository, Mockito.never()).flush();
		Mockito.verifyNoMoreInteractions(repository);
	}  

}
