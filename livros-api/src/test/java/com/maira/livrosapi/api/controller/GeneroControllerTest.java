package com.maira.livrosapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.api.assembler.GeneroInputDisassembler;
import com.maira.livrosapi.api.assembler.GeneroModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.GeneroModel;
import com.maira.livrosapi.api.model.input.GeneroInput;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GeneroNaoEncontradoException;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.service.GeneroService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {GeneroController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class GeneroControllerTest {

	@MockBean
	GeneroService service;
	
	@MockBean
	GeneroModelAssembler generoModelAssembler;

	@MockBean
	GeneroInputDisassembler generoInputDisassembler;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	private Genero genero;
	private GeneroModel generoModel;
	private GeneroInput generoInput;
	private Genero generoSemId;
	private Long generoId;
	
	@BeforeEach
	public void init() {
		
		generoId = 1L;
		generoInput = GeneroInput.builder().descricao("Romance").build();
		generoSemId = Genero.builder().descricao("Romance").build();
		genero = Genero.builder().id(1L).descricao("Romance").build();
		generoModel = GeneroModel.builder().id(1L).descricao("Romance").build();
		
	}
	
	
	@Test
	@DisplayName("Quando chamar GET Entao deve retornar status 200")
	void listarGernero_RetornaOK() throws Exception {
		
		when(service.listByContaining(anyString(), any(Pageable.class)))
		.thenAnswer(answer -> {
			Pageable pageableParametro = answer.getArgument(1, Pageable.class);
			Page<Genero> pageGenero = new PageImpl<Genero>(Collections.singletonList(genero), pageableParametro, 1);
			return pageGenero;
		});
		
		when(generoModelAssembler.toCollectionModel(anyList()))
			.thenReturn(Collections.singletonList(generoModel));
				
		mockMvc.perform(get("/generos"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content").isArray())
		.andExpect(jsonPath("$.content", Matchers.hasSize(1)))
		.andExpect(jsonPath("$['pageable']['paged']").value("true"))
		.andDo(print());
	}
	
	@Test
	@DisplayName("Quando chamar GET passando descricao Entao deve retornar status 200")
	void listarGenero_PorDescricao_RetornaListaFiltradaEStatusOK() throws Exception {
		when(service.listByContaining(anyString(), any(Pageable.class)))
		.thenAnswer(answer -> {
			Pageable pageableParametro = answer.getArgument(1, Pageable.class);
			Page<Genero> pageGenero = new PageImpl<Genero>(Collections.singletonList(genero), pageableParametro, 1);
			return pageGenero;
		});

		when(generoModelAssembler.toCollectionModel(anyList()))
			.thenReturn(Collections.singletonList(generoModel));

		mockMvc.perform(get("/generos")
				.param("descricao", genero.getDescricao()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content").isArray())
		.andExpect(jsonPath("$.content", Matchers.hasSize(1)))
		.andExpect(jsonPath("$.content[0].descricao").value(genero.getDescricao()))
		.andExpect(jsonPath("$['pageable']['paged']").value("true"))
		.andDo(print());
	}

	@Test
	@DisplayName("Quando chamar GET passando descricao inexistente Entao deve retornar lista vazia")
	void listarGenero_PorDescricaoInexistente_RetornaListaVaziaStatusOK() throws Exception {
		when(service.listByContaining(anyString(), any(Pageable.class)))
				.thenAnswer(answer -> {
					Pageable pageableParametro = answer.getArgument(1, Pageable.class);
					Page<Genero> pageGenero = new PageImpl<Genero>(Collections.emptyList(), pageableParametro, 1);
					return pageGenero;
				});

		when(generoModelAssembler.toCollectionModel(anyList()))
				.thenReturn(Collections.emptyList());

		mockMvc.perform(get("/generos")
						.param("descricao", genero.getDescricao()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content", Matchers.empty()))
				.andExpect(jsonPath("$['pageable']['paged']").value("true"))
				.andDo(print());
	}
	
	@Test
	@DisplayName("Quando chamar GET passando pageable Entao deve retornar status 200")
	void listarGenero_PassandoPageable_RetornaListaEStatusOK() throws Exception {

		when(service.listByContaining(anyString(), any(Pageable.class)))
		.thenAnswer(answer -> {
			Pageable pageableParametro = answer.getArgument(1, Pageable.class);
			Page<Genero> pageGenero = new PageImpl<Genero>(Collections.singletonList(genero), pageableParametro, 1);
			return pageGenero;
		});
		
		when(generoModelAssembler.toCollectionModel(anyList()))
			.thenReturn(Collections.singletonList(generoModel));

		mockMvc.perform(get("/generos")
				.param("page", "0")
		        .param("size", "20")
		        .param("sort", "descricao,asc"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content").isArray())
		.andExpect(jsonPath("$.content", Matchers.hasSize(1)))
		.andExpect(jsonPath("$['pageable']['paged']").value("true"))
		.andExpect(jsonPath("$['sort']['sorted']").value("true"))
		.andDo(print());
	}
	
	
	@Test
	@DisplayName("Dado um generoId valido Quando chamar GET Entao deve retornar o genero")
	void buscarGenero_ComGeneroIdValido_RetornaGenero() throws Exception {
		when(service.buscarOuFalhar(anyLong())).thenReturn(genero);
		
		when(generoModelAssembler.toModel(any(Genero.class)))
		.thenAnswer(answer -> {
			return generoModel;
		});
		
		mockMvc.perform(get("/generos/{generoId}",generoId))
		.andExpect(status().isOk())
		.andDo(print());
		
		verify(service, times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(service);		
	}
	
	@Test
	@DisplayName("Dado um generoId invalido Quando chamar GET Entao deve retornar status 404")
	void buscarGenero_ComGeneroIdInValido_RetornaNotFound() throws Exception {
		when(service.buscarOuFalhar(generoId)).thenThrow(new GeneroNaoEncontradoException(generoId));

		mockMvc.perform(get("/generos/{generoId}",generoId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(GeneroNaoEncontradoException.class));
		
		verify(service, times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(service);
	}
	
	
	@Test
	@DisplayName("Dado um genero valido Quando chamar POST Entao deve retornar status 201")
	void adicionarGenero_ComDadosValidos_RetornaCreated() throws Exception {
		when(generoInputDisassembler.toDomainObject(any(GeneroInput.class))).thenReturn(generoSemId);
		when(service.salvar(any(Genero.class))).thenReturn(genero);
		when(generoModelAssembler.toModel(any(Genero.class))).thenReturn(generoModel);
		
		mockMvc.perform(post("/generos")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(generoInput)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").exists());
		
		verify(service, times(1)).salvar(any(Genero.class));
		verifyNoMoreInteractions(service);	
	}

	@Test
	@DisplayName("Dado um genero com descricao existente Quando chamar POST Entao deve retornar status 409")
	void adicionarGenero_ComDescricaoGeneroExistente_RetornaConflict() throws Exception {
		when(generoInputDisassembler.toDomainObject(Mockito.any(GeneroInput.class))).thenReturn(generoSemId);
		when(service.salvar(Mockito.any(Genero.class)))
				.thenThrow(new EntidadeEmUsoException(String.format("Genero de descrição %s já cadastrado",
						generoSemId.getDescricao())));

		mockMvc.perform(post("/generos")
						.contentType("application/json")
						.accept(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(generoInput)))
				.andExpect(status().isConflict())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class));

		verify(service, Mockito.times(1)).salvar(Mockito.any(Genero.class));
		verifyNoMoreInteractions(service);
	}
	
	
	@Test
	@DisplayName("Dado um genero valido Quando chamar PUT Entao deve retornar status 200")
	void atualizarGenero_ComGeneroValido_RetornarOK() throws Exception {
		when(service.buscarOuFalhar(anyLong())).thenReturn(genero);
		when(service.salvar(any(Genero.class))).thenReturn(genero);
		when(generoModelAssembler.toModel(any(Genero.class))).thenReturn(generoModel);
		
		mockMvc.perform(put("/generos/{generoId}", generoId)
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(generoInput)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.id").value(generoId));
		
		verify(service, times(1)).salvar(any(Genero.class));
	}
	
	
	@Test
	@DisplayName("Dado um generoId invalido Quando chamar PUT Entao deve retornar status 404")
	void atualizarGenero_ComGeneroIdInvalido_RetornarNotFound() throws Exception {
		when(service.buscarOuFalhar(generoId)).thenThrow(new GeneroNaoEncontradoException(generoId));
		
		mockMvc.perform(put("/generos/{generoId}", generoId)
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(generoInput)))
		.andExpect(status().isNotFound())
		.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(GeneroNaoEncontradoException.class));
		
		verify(service, never()).salvar(any(Genero.class));
	}
	
	@Test
	@DisplayName("Dado um generoId valido Quando chamar metodo excluir Entao deve retornar status 204")
	void removerGenero_ComGeneroIdValido_RetornarNoContent() throws Exception {
		mockMvc.perform(delete("/generos/{generoId}",generoId)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent())
		.andReturn();

		verify(service, times(1)).excluir(anyLong());
		verifyNoMoreInteractions(service);	
	}
	
	@Test
	@DisplayName("Dado um generoId invalido Quando chamar metodo excluir Entao deve retornar status 404")
	void removerGenero_ComGeneroIdInvalido_RetornarNotFound() throws Exception {
		doThrow(new GeneroNaoEncontradoException(generoId)).when(service).excluir(generoId);

		mockMvc.perform(delete("/generos/{generoId}",generoId)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Dado um generoId valido em uso Quando chamar metodo excluir Entao deve retornar status 409")
	void removerGenero_ComGeneroIdEmUso_RetornarConfict() throws Exception {
		doThrow(EntidadeEmUsoException.class).when(service).excluir(anyLong());

		mockMvc.perform(delete("/generos/{generoId}",generoId)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isConflict())
		.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class))
		.andReturn();

		verify(service, times(1)).excluir(anyLong());
		verifyNoMoreInteractions(service);	
	} 

}
