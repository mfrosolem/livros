package com.maira.livrosapi.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.api.assembler.GeneroInputDisassembler;
import com.maira.livrosapi.api.assembler.GeneroModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.GeneroModel;
import com.maira.livrosapi.api.model.input.GeneroInput;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GeneroNaoEncontradoException;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.repository.GeneroRepository;
import com.maira.livrosapi.domain.service.GeneroService;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GeneroControllerTest {
	
	@InjectMocks
	GeneroController controller;
	
	@Mock
	GeneroService service;
	
	@Mock
	GeneroRepository repository;
	
	@Mock
	GeneroModelAssembler generoModelAssembler;

	@Mock
	GeneroInputDisassembler generoInputDisassembler;
	
	MockMvc mockMvc;
	
	private Genero genero;
	private GeneroModel generoModel;
	private GeneroInput generoInput;
	private Genero generoSemId;
	private Long generoId;
	
	@BeforeEach
	public void init() {
		
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.alwaysDo(print())
				.setControllerAdvice(ApiExceptionHandler.class)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
		
		generoId = 1L;
		generoInput = GeneroInput.builder().descricao("Romance").build();
		generoSemId = Genero.builder().descricao("Romance").build();
		genero = Genero.builder().id(1L).descricao("Romance").build();
		generoModel = GeneroModel.builder().id(1L).descricao("Romance").build();
		
	}
	
	
	@Test
	void Quando_chamar_GET_Entao_deve_retornar_status_200() throws Exception {
		
		when(repository.findByDescricaoContaining(anyString(), Mockito.any(Pageable.class)))
		.thenAnswer(answer -> {
			Pageable pageableParametro = answer.getArgument(1, Pageable.class);
			Page<Genero> pageGenero = new PageImpl<Genero>(Collections.singletonList(genero), pageableParametro, 1);
			return pageGenero;
		});
		
		when(generoModelAssembler.toCollectionModel(Mockito.anyList()))
			.thenReturn(Collections.singletonList(generoModel));
				
		mockMvc.perform(get("/generos"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$['pageable']['paged']").value("true"))
		.andDo(MockMvcResultHandlers.print());
		
		verify(repository, Mockito.times(1)).findByDescricaoContaining(anyString(), Mockito.any(Pageable.class));
		
	}
	
	@Test
	void Quando_chamar_GET_passando_descricao_Entao_deve_retornar_status_200() throws Exception {
		var descricaoFiltro = "Romance";
		when(repository.findByDescricaoContaining(anyString(), Mockito.any(Pageable.class)))
		.thenAnswer(answer -> {
			Pageable pageableParametro = answer.getArgument(1, Pageable.class);
			Page<Genero> pageGenero = new PageImpl<Genero>(Collections.singletonList(genero), pageableParametro, 1);
			return pageGenero;
		});

		when(generoModelAssembler.toCollectionModel(Mockito.anyList()))
			.thenReturn(Collections.singletonList(generoModel));

		mockMvc.perform(get("/generos")
				.param("descricao", descricaoFiltro))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].descricao").value(descricaoFiltro))
		.andExpect(MockMvcResultMatchers.jsonPath("$['pageable']['paged']").value("true"))
		.andDo(MockMvcResultHandlers.print());
		
		verify(repository, Mockito.times(1)).findByDescricaoContaining(anyString(), Mockito.any(Pageable.class));
		
	}
	
	@Test
	void Quando_chamar_GET_passando_pageable_Entao_deve_retornar_status_200() throws Exception {

		when(repository.findByDescricaoContaining(anyString(), Mockito.any(Pageable.class)))
		.thenAnswer(answer -> {
			Pageable pageableParametro = answer.getArgument(1, Pageable.class);
			Page<Genero> pageGenero = new PageImpl<Genero>(Collections.singletonList(genero), pageableParametro, 1);
			return pageGenero;
		});
		
		when(generoModelAssembler.toCollectionModel(Mockito.anyList()))
			.thenReturn(Collections.singletonList(generoModel));

		mockMvc.perform(get("/generos")
				.param("page", "0")
		        .param("size", "20")
		        .param("sort", "descricao,asc"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$['pageable']['paged']").value("true"))
		.andExpect(MockMvcResultMatchers.jsonPath("$['sort']['sorted']").value("true"))
		.andDo(MockMvcResultHandlers.print());
		
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		
		verify(repository, Mockito.times(1)).findByDescricaoContaining(anyString(), pageableCaptor.capture());
		PageRequest pageable = (PageRequest) pageableCaptor.getValue();
	    
	    assertEquals(0, pageable.getPageNumber());
	    assertEquals(20, pageable.getPageSize());

	}
	
	
	@Test
	void Dado_um_generoId_valido_Quando_chamar_GET_Entao_deve_retornar_o_genero() throws Exception {
		Mockito.when(service.buscarOuFalhar(anyLong())).thenReturn(genero);
		
		Mockito.when(generoModelAssembler.toModel(Mockito.any(Genero.class)))
		.thenAnswer(answer -> {
			return generoModel;
		});
		
		mockMvc.perform(MockMvcRequestBuilders.get("/generos/{generoId}",generoId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print());
		
	
		verify(service, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(service);		
	}
	
	@Test
	void Dado_um_generoId_invalido_Quando_chamar_GET_Entao_deve_retornar_status_404() throws Exception {
		when(service.buscarOuFalhar(generoId)).thenThrow(new GeneroNaoEncontradoException(generoId));

		mockMvc.perform(get("/generos/{generoId}",generoId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof GeneroNaoEncontradoException));
		
		verify(service, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(service);
	}
	
	
	@Test
	void Dado_um_genero_valido_Quando_chamar_POST_Entao_deve_retornar_status_201() throws Exception { 
		when(generoInputDisassembler.toDomainObject(Mockito.any(GeneroInput.class))).thenReturn(generoSemId);
		
		when(service.salvar(Mockito.any(Genero.class))).thenReturn(genero);
		
		when(generoModelAssembler.toModel(Mockito.any(Genero.class))).thenReturn(generoModel);
		
		var objectMapper = new ObjectMapper();
		
		mockMvc.perform(post("/generos")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(generoInput)))
		.andExpect(status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
		
		verify(service, Mockito.times(1)).salvar(Mockito.any(Genero.class));
		verifyNoMoreInteractions(service);	
		
	}
	
	
	@Test
	void Dado_um_genero_valido_Quando_chamar_PUT_Entao_deve_retornar_status_200() throws Exception { 
		
		Mockito.when(service.buscarOuFalhar(anyLong())).thenReturn(genero);
		
		when(service.salvar(Mockito.any(Genero.class))).thenReturn(genero);
		
		when(generoModelAssembler.toModel(Mockito.any(Genero.class))).thenReturn(generoModel);
		
		var objectMapper = new ObjectMapper();
		
		mockMvc.perform(put("/generos/{generoId}", generoId)
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(generoInput)))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(generoId));
		
		verify(service, Mockito.times(1)).salvar(Mockito.any(Genero.class));
		verifyNoMoreInteractions(service);	
		
	}
	
	
	@Test
	void Dado_um_generoId_invalido_Quando_chamar_PUT_Entao_deve_retornar_status_404() throws Exception { 
		when(service.buscarOuFalhar(generoId)).thenThrow(new GeneroNaoEncontradoException(generoId));
		
		var objectMapper = new ObjectMapper();
		
		mockMvc.perform(put("/generos/{generoId}", generoId)
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(generoInput)))
		.andExpect(status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof GeneroNaoEncontradoException));
		
		verify(service, Mockito.never()).salvar(Mockito.any(Genero.class));
		verifyNoMoreInteractions(service);	
		
	}
	
	@Test
	void Dado_um_generoId_valido_Quando_chamar_metodo_excluir_Entao_deve_retornar_status_204() throws Exception {
		//Mockito.doNothing().when(service).excluir(Mockito.anyLong());

		mockMvc.perform(delete("/generos/{generoId}",generoId)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isNoContent())
		.andReturn();

		verify(service, Mockito.times(1)).excluir(anyLong());
		verifyNoMoreInteractions(service);	
		
	} 
	
	@Test
	void Dado_um_generoId_invalido_Quando_chamar_metodo_excluir_Entao_deve_retornar_status_404() throws Exception {
		Mockito.doThrow(GeneroNaoEncontradoException.class).when(service).excluir(Mockito.anyLong());

		mockMvc.perform(delete("/generos/{generoId}",generoId)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof GeneroNaoEncontradoException))
		.andReturn();

		verify(service, Mockito.times(1)).excluir(anyLong());
		verifyNoMoreInteractions(service);	
	} 
	
	@Test
	void Dado_um_generoId_valido_em_uso_Quando_chamar_metodo_excluir_Entao_deve_retornar_status_409() throws Exception {
		Mockito.doThrow(EntidadeEmUsoException.class).when(service).excluir(Mockito.anyLong());

		mockMvc.perform(delete("/generos/{generoId}",generoId)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isConflict())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntidadeEmUsoException))
		.andReturn();

		verify(service, Mockito.times(1)).excluir(anyLong());
		verifyNoMoreInteractions(service);	
	} 

}
