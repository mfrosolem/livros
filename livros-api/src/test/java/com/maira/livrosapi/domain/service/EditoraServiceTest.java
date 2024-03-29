package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.EditoraNaoEncontradaException;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.repository.EditoraRepository;
import com.maira.livrosapi.domain.service.impl.EditoraServiceImpl;
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
class EditoraServiceTest {
	
	@InjectMocks
	EditoraServiceImpl service;
	
	@Mock
	EditoraRepository repository;
	
	Editora editora;
	
	Long editoraId;
	
	@BeforeEach
	void init() {
		editora = Editora.builder().nome("Belas Letras").build();
		editoraId = 1L;
	}
	
	@Test
	@DisplayName("Dado uma editoraId valida Quando chamar metodo buscarOuFalhar Entao deve retornar uma editora")
	void Dado_uma_editoraId_valida_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_uma_editora() {
		when(repository.findById(anyLong())).thenAnswer(answer -> {
			Long editoraIdPassada = answer.getArgument(0, Long.class);
			editora.setId(editoraIdPassada);
			return Optional.of(editora);
		});

		Editora sut = service.buscarOuFalhar(editoraId);

		assertThat(sut).isInstanceOf(Editora.class);
		assertThat(sut.getId()).isNotNull();
		verify(repository, times(1)).findById(editoraId);
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("Dado uma editoraId invalida Quando chamar metodo buscarOuFalhar Entao deve lancar exception EditoraNaoEncontradaException")
	void Dado_uma_editoraId_invalida_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_EditoraNaoEncontradaException() {
		when(repository.findById(anyLong()))
			.thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.buscarOuFalhar(editoraId)).isInstanceOf(EditoraNaoEncontradaException.class);
		verify(repository, times(1)).findById(editoraId);
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("Dado uma editora valida Quando salvar Entao deve retornar uma editora com id")
	void Dado_uma_editora_valida_Quando_salvar_Entao_deve_retornar_uma_editora_com_id() {
		when(repository.save(any(Editora.class)))
			.thenAnswer(answer -> {
				Editora editoraPassada = answer.getArgument(0, Editora.class);
				editoraPassada.setId(editoraId);
				return editoraPassada;
			});

		Editora sut = service.salvar(editora);

		assertThat(sut).isInstanceOf(Editora.class);
		assertThat(sut.getId()).isNotNull();
	}

	@Test
	@DisplayName("Dado uma editora valida Quando salvar Entao deve chamar metodo save do repository")
	void Dado_uma_editora_valida_Quando_salvar_Entao_deve_chamar_metodo_save_do_repository() {
		when(repository.save(any(Editora.class)))
			.thenAnswer(answer -> {
				Editora editoraPassada = answer.getArgument(0, Editora.class);
				editoraPassada.setId(editoraId);
				return editoraPassada;
			});

		service.salvar(editora);

		verify(repository, times(1)).save(editora);
		verifyNoMoreInteractions(repository);
	}


	@Test
	@DisplayName("Dado uma editora que ja existe Quando salvar Entao deve lancar exception EntidadeEmUsoException")
	void Dado_uma_editora_que_ja_existe_Quando_salvar_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		when(repository.save(any(Editora.class))).thenThrow(DataIntegrityViolationException.class);

		assertThatThrownBy(() -> service.salvar(editora)).isInstanceOf(EntidadeEmUsoException.class);
		verify(repository, times(1)).save(any(Editora.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("Dado uma editoraId valida Quando chamar metodo excluir Entao deve excluir editora")
	void Dado_uma_editoraId_valida_Quando_chamar_metodo_excluir_Entao_deve_excluir_editora() {
		assertThatCode(() -> service.excluir(editoraId)).doesNotThrowAnyException();
		verify(repository, times(1)).deleteById(anyLong());
		verify(repository, times(1)).flush();
		verifyNoMoreInteractions(repository);
	}


	@Test
	@DisplayName("Dado uma editoraId invalida Quando chamar metodo excluir Entao deve lancar exception EditoraNaoEncontradaException")
	void Dado_uma_editoraId_invalida_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EditoraNaoEncontradaException() {
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());

		assertThatThrownBy(()-> service.excluir(editoraId)).isInstanceOf(EditoraNaoEncontradaException.class);
		verify(repository, times(1)).deleteById(anyLong());
		verify(repository, never()).flush();
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("Dado uma editoraId em uso Quando chamar metodo excluir Entao deve lancar exception EntidadeEmUsoException")
	void Dado_uma_editoraId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());

		assertThatThrownBy(() -> service.excluir(editoraId)).isInstanceOf(EntidadeEmUsoException.class);
		verify(repository, times(1)).deleteById(anyLong());
		verify(repository, never()).flush();
		verifyNoMoreInteractions(repository);
	}

	@Test
	@DisplayName("Quando chamar metodo listByNameContaining Entao deve retornar todos os editoras")
	void listByNameContaining_RetornaTodosEditoras() {
		List<Editora> editoras = new ArrayList<>() {
			{
				add(Editora.builder().id(1L).nome("Belas Letras").build());
				add(Editora.builder().id(2L).nome("Companhia das Letras").build());
				add(Editora.builder().id(3L).nome("Grupo Record").build());
			}
		};
		Pageable pageable = PageRequest.of(0,20);
		Page<Editora> page = new PageImpl<>(editoras,pageable, editoras.size());
		when(repository.findByNomeContaining(anyString(), any(Pageable.class))).thenReturn(page);

		Page<Editora> sut = service.listByContaining("", pageable);

		assertThat(sut).isNotEmpty().hasSize(3);
	}

	@Test
	@DisplayName("Quando chamar metodo listByDescricaoContaining filtrando por nome inexistente Entao deve retornar lista vazia")
	void listByNomeContaining_Filtrando_RetornaListaVazia() {
		List<Editora> editoras = new ArrayList<>();
		Pageable pageable = PageRequest.of(0,20);
		Page<Editora> page = new PageImpl<>(editoras,pageable, editoras.size());
		when(repository.findByNomeContaining(anyString(), any(Pageable.class))).thenReturn(page);

		Page<Editora> sut = service.listByContaining("Romance", pageable);
		assertThat(sut).isEmpty();
	}
	
}
