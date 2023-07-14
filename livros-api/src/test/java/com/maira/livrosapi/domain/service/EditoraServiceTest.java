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

import com.maira.livrosapi.domain.exception.EditoraNaoEncontradaException;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.repository.EditoraRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class EditoraServiceTest {
	
	@InjectMocks
	EditoraService service;
	
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
	void Dado_uma_editoraId_valida_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_uma_editora() {
		when(repository.findById(anyLong())).thenAnswer(answer -> {
			Long editoraIdPassada = answer.getArgument(0, Long.class);
			editora.setId(editoraIdPassada);
			return Optional.of(editora);
		});
		
		Editora editoraRetornada = service.buscarOuFalhar(editoraId);
		
		assertInstanceOf(Editora.class, editoraRetornada);
		assertEquals(editoraId, editoraRetornada.getId());
		verify(repository, Mockito.times(1)).findById(editoraId);
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_uma_editoraId_invalida_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_EditoraNaoEncontradaException() {
		when(repository.findById(anyLong()))
			.thenThrow(new EditoraNaoEncontradaException(editoraId));
		
		assertThrows(EditoraNaoEncontradaException.class, () -> service.buscarOuFalhar(editoraId));
		verify(repository, Mockito.times(1)).findById(editoraId);
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_uma_editora_valida_Quando_salvar_Entao_deve_retornar_uma_editora_com_id() {
		when(repository.save(Mockito.any(Editora.class)))
			.thenAnswer(answer -> {
				Editora editoraPassada = answer.getArgument(0, Editora.class);
				editoraPassada.setId(editoraId);
				return editoraPassada;
			});
		
		Editora editoraSalva = service.salvar(editora);
		
		assertInstanceOf(Editora.class, editoraSalva);
		assertEquals(editoraId, editoraSalva.getId());
	}
	
	@Test
	void Dado_uma_editora_valida_Quando_salvar_Entao_deve_chamar_metodo_save_do_repository() {
		when(repository.save(Mockito.any(Editora.class)))
			.thenAnswer(answer -> {
				Editora editoraPassada = answer.getArgument(0, Editora.class);
				editoraPassada.setId(editoraId);
				return editoraPassada;
			});
		
		service.salvar(editora);
		
		verify(repository, Mockito.times(1)).save(editora);
		verifyNoMoreInteractions(repository);
	}
	

	@Test
	void Dado_uma_editora_valida_Quando_salvar_Entao_deve_lancar_exception_EntidadeEmUsoException_se_editora_ja_existe() {
		when(repository.save(Mockito.any(Editora.class)))
			.thenThrow(new EntidadeEmUsoException(
					String.format("Editora de nome %s já cadastrada", editora.getNome())));
		
		assertThrows(EntidadeEmUsoException.class, () -> service.salvar(editora));
		verify(repository, Mockito.times(1)).save(editora);
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_uma_editoraId_valida_Quando_chamar_metodo_excluir_Entao_deve_excluir_editora() {
		Mockito.doNothing().when(repository).deleteById(Mockito.anyLong());
		Mockito.doNothing().when(repository).flush();
		
		service.excluir(editoraId);
		
		verify(repository, Mockito.times(1)).deleteById(editoraId);
		verify(repository, Mockito.times(1)).flush();
		verifyNoMoreInteractions(repository);
	}
	
	
	@Test
	void Dado_uma_editoraId_invalida_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EditoraNaoEncontradaException() {
		Mockito.doThrow(new EditoraNaoEncontradaException(editoraId))
			.when(repository).deleteById(editoraId);
		
		assertThrows(EditoraNaoEncontradaException.class, ()-> service.excluir(editoraId));
		verify(repository, Mockito.times(1)).deleteById(editoraId);
		verify(repository, Mockito.never()).flush();
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	void Dado_uma_editoraId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
		Mockito.doThrow(new EntidadeEmUsoException(
				String.format("Editora de código %d não pode ser removida, pois está em uso", editoraId)))
			.when(repository).deleteById(editoraId);
		
		assertThrows(EntidadeEmUsoException.class, () -> service.excluir(editoraId));
		verify(repository, Mockito.times(1)).deleteById(editoraId);
		verify(repository, Mockito.never()).flush();
		verifyNoMoreInteractions(repository);
	}
	
	
}
