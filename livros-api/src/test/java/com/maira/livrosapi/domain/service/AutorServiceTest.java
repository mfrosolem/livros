package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.AutorNaoEncontradoException;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.repository.AutorRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

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
	@DisplayName("Dado um autorId valido Quando chamar metodo buscarOuFalhar Entao deve retornar um autor")
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
	@DisplayName("Dado um autorId invalido Quando chamar metodo buscarOuFalhar Entao deve lancar exception AutorNaoEncontradoException")
	void Dado_um_autorId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_AutorNaoEncontradoException() {
		when(repository.findById(anyLong()))
			.thenThrow(new AutorNaoEncontradoException(autorId));
		
		assertThrows(AutorNaoEncontradoException.class, () -> service.buscarOuFalhar(autorId));
		verify(repository, Mockito.times(1)).findById(autorId);
	}
	
	@Test
	@DisplayName("Dado um autor valido Quando salvar Entao deve retornar um autor com id")
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
	@DisplayName("Dado um autor valido Quando salvar Entao deve chamar metodo save do repository")
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
	@DisplayName("Dado um autor que ja existe Quando salvar Entao deve lancar exception EntidadeEmUsoException")
	void Dado_um_autor_que_ja_existe_Quando_salvar_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		when(repository.save(any(Autor.class))).thenThrow(DataIntegrityViolationException.class);
		
		assertThrows(EntidadeEmUsoException.class, () -> service.salvar(autor));
		verify(repository, Mockito.times(1)).save(Mockito.any(Autor.class));
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	@DisplayName("Dado um autorId valido Quando chamar metodo excluir Entao deve excluir autor")
	void Dado_um_autorId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_autor() {
		service.excluir(autorId);
		
		verify(repository, Mockito.times(1)).deleteById(anyLong());
		verify(repository, Mockito.times(1)).flush();
	}
	
	@Test
	@DisplayName("Dado um autorId invalido Quando chamar metodo excluir Entao deve lancar exception AutorNaoEncontradoException")
	void Dado_um_autorId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_AutorNaoEncontradoException() {
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());
		
		assertThrows(AutorNaoEncontradoException.class, () -> service.excluir(autorId));
		verify(repository, Mockito.times(1)).deleteById(anyLong());
		verify(repository, Mockito.never()).flush();
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	@DisplayName("Dado um autorId em uso Quando chamar metodo excluir Entao deve lancar exception EntidadeEmUsoException")
	void Dado_um_autorId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());
		
		assertThrows(EntidadeEmUsoException.class, () -> service.excluir(autorId));
		verify(repository, Mockito.times(1)).deleteById(anyLong());
		verify(repository, Mockito.never()).flush();
		verifyNoMoreInteractions(repository);
	}
	 
	 
}
