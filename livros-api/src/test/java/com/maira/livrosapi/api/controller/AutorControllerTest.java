package com.maira.livrosapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.api.assembler.AutorInputDisassembler;
import com.maira.livrosapi.api.assembler.AutorModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.AutorModel;
import com.maira.livrosapi.api.model.input.AutorInput;
import com.maira.livrosapi.domain.exception.AutorNaoEncontradoException;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.service.AutorService;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@ContextConfiguration(classes = {AutorController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class AutorControllerTest {

	@MockBean
	AutorService service;

	@MockBean
	AutorModelAssembler autorModelAssembler;

	@MockBean
	AutorInputDisassembler autorInputDisassembler;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Autor autor;
	private Autor autorSemId;
	private AutorInput autorInput;
	private AutorModel autorModel;
	private Long autorId;
	
	@BeforeEach
	public void init() {
		
		autor = Autor.builder().id(1L).nome("Joaquim Maria").sobrenome("Machado de Assis")
				.nomeConhecido("Machado de Assis").sexo("M").build();
		
		autorSemId = Autor.builder().nome("Joaquim Maria").sobrenome("Machado de Assis")
				.nomeConhecido("Machado de Assis").sexo("M").build();
		
		autorInput = AutorInput.builder().nome("Joaquim Maria").sobrenome("Machado de Assis")
				.nomeConhecido("Machado de Assis").sexo("M").build();
		
		autorModel = AutorModel.builder().id(1L).nome("Joaquim Maria").sobrenome("Machado de Assis")
				.nomeConhecido("Machado de Assis").sexo("M").build();
		
		autorId = 1L;
		
	}
	
	
	@Test
	@DisplayName("Quando chamar GET Entao deve retornar status 200")
	void listarAutor_RetornaOK() throws Exception {
		when(service.listByNameContaining(anyString(), Mockito.any(Pageable.class)))
		.thenAnswer(answer -> {
			Pageable pageableParametro = answer.getArgument(1, Pageable.class);
			Page<Autor> pageAutor = new PageImpl<Autor>(Collections.singletonList(autor), pageableParametro, 1);
			return pageAutor;
		});
		
		when(autorModelAssembler.toCollectionModel(Mockito.anyList()))
			.thenReturn(Collections.singletonList(autorModel));
				
		mockMvc.perform(get("/autores"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content").isArray())
		.andExpect(jsonPath("$.content", Matchers.hasSize(1)))
		.andExpect(jsonPath("$['pageable']['paged']").value("true"))
		.andDo(print());
	}
	
	@Test
	@DisplayName("Quando chamar GET passando nome Entao deve retornar status 200")
	void listarAutor_PorNome_RetornaListaFiltradaEStatusOK() throws Exception {
		when(service.listByNameContaining(anyString(), Mockito.any(Pageable.class)))
		.thenAnswer(answer -> {
			Pageable pageableParametro = answer.getArgument(1, Pageable.class);
			Page<Autor> pageAutor = new PageImpl<Autor>(Collections.singletonList(autor), pageableParametro, 1);
			return pageAutor;
		});

		when(autorModelAssembler.toCollectionModel(Mockito.anyList()))
			.thenReturn(Collections.singletonList(autorModel));

		mockMvc.perform(get("/autores")
				.param("nome", autor.getNome()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content").isArray())
		.andExpect(jsonPath("$.content", Matchers.hasSize(1)))
		.andExpect(jsonPath("$.content[0].nome").value(autor.getNome()))
		.andExpect(jsonPath("$['pageable']['paged']").value("true"))
		.andDo(print());
	}

	@Test
	@DisplayName("Quando chamar GET passando nome inexistente Entao deve retornar lista vazia")
	void listarAutor_PorNomeInexistente_RetornaListaVaziaStatusOK() throws Exception {
		when(service.listByNameContaining(anyString(), Mockito.any(Pageable.class)))
				.thenAnswer(answer -> {
					Pageable pageableParametro = answer.getArgument(1, Pageable.class);
					Page<Autor> pageAutor = new PageImpl<Autor>(Collections.emptyList(), pageableParametro, 1);
					return pageAutor;
				});

		when(autorModelAssembler.toCollectionModel(Mockito.anyList()))
				.thenReturn(Collections.emptyList());

		mockMvc.perform(get("/autores")
						.param("nome", autor.getNome()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content", Matchers.empty()))
				.andExpect(jsonPath("$['pageable']['paged']").value("true"))
				.andDo(print());
	}
	
	@Test
	@DisplayName("Quando chamar GET passando pageable Entao deve retornar status 200")
	void listarAutor_PassandoPageable_RetornaListaEStatusOK () throws Exception {

		when(service.listByNameContaining(anyString(), Mockito.any(Pageable.class)))
		.thenAnswer(answer -> {
			Pageable pageableParametro = answer.getArgument(1, Pageable.class);
			Page<Autor> pageAutor = new PageImpl<Autor>(Collections.singletonList(autor), pageableParametro, 1);
			return pageAutor;
		});
		
		when(autorModelAssembler.toCollectionModel(Mockito.anyList()))
			.thenReturn(Collections.singletonList(autorModel));

		mockMvc.perform(get("/autores")
				.param("page", "0")
		        .param("size", "20")
		        .param("sort", "nome,asc"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content").isArray())
		.andExpect(jsonPath("$.content", Matchers.hasSize(1)))
		.andExpect(jsonPath("$['pageable']['paged']").value("true"))
		.andExpect(jsonPath("$['sort']['sorted']").value("true"))
		.andDo(print());
	}


	@Test
	@DisplayName("Dado um autorId valido Quando chamar GET Entao deve retornar o autor")
	void buscarAutor_ComAutorIdValido_RetornaAutor () throws Exception {
		when(service.buscarOuFalhar(anyLong())).thenReturn(autor);
		when(autorModelAssembler.toModel(Mockito.any(Autor.class))).thenReturn(autorModel);

		mockMvc.perform(get("/autores/{autorId}", autorId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(autorModel.getId()))
				.andDo(print());

		verify(service, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(service);
	}
	
	
	@Test
	@DisplayName("Dado um autorId invalido Quando chamar GET Entao deve retornar status 404")
	void buscarAutor_ComAutorIdInvalido_RetornaNotFound() throws Exception {
		when(service.buscarOuFalhar(autorId)).thenThrow(new AutorNaoEncontradoException(autorId));

		mockMvc.perform(get("/autores/{autorId}",autorId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(AutorNaoEncontradoException.class));
		
		verify(service, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(service);
	}
	
	@Test
	@DisplayName("Dado um autor valido Quando chamar POST Entao deve retornar status 201")
	void adicionarAutor_ComAutorValido_RetornaCreated() throws Exception {
		when(autorInputDisassembler.toDomainObject(Mockito.any(AutorInput.class))).thenReturn(autorSemId);
		when(service.salvar(Mockito.any(Autor.class))).thenReturn(autor);
		when(autorModelAssembler.toModel(Mockito.any(Autor.class))).thenReturn(autorModel);

		mockMvc.perform(post("/autores")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(autorInput)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").exists());

		verify(service, Mockito.times(1)).salvar(Mockito.any(Autor.class));
		verifyNoMoreInteractions(service);		
	}

	@Test
	@DisplayName("Dado um autor com nome e sobrenome existente Quando chamar POST Entao deve retornar status 409")
	void adicionarAutor_ComNomeSobrenomeAutorExistente_RetornaConflict() throws Exception {
		when(autorInputDisassembler.toDomainObject(Mockito.any(AutorInput.class))).thenReturn(autorSemId);
		when(service.salvar(Mockito.any(Autor.class)))
				.thenThrow(new EntidadeEmUsoException(String.format("Autor(a) de nome %s %s já cadastrado(a)",
						autorSemId.getNome(), autorSemId.getSobrenome())));

		mockMvc.perform(post("/autores")
						.contentType("application/json")
						.accept(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(autorInput)))
				.andExpect(status().isConflict())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class));

		verify(service, Mockito.times(1)).salvar(Mockito.any(Autor.class));
		verifyNoMoreInteractions(service);
	}
	
	
	@Test
	@DisplayName("Dado um autor valido Quando chamar PUT Entao deve retornar status 200")
	void atualizarAutor_ComAutorValido_RetornaOK() throws Exception {
		Mockito.when(service.buscarOuFalhar(anyLong())).thenReturn(autor);
		when(service.salvar(Mockito.any(Autor.class))).thenReturn(autor);
		when(autorModelAssembler.toModel(Mockito.any(Autor.class))).thenReturn(autorModel);
		
		mockMvc.perform(put("/autores/{autorId}", autorId)
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(autorInput)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.id").value(autorId));
	}
	
	@Test
	@DisplayName("Dado um autorId invalido Quando chamar PUT Entao deve retornar status 404")
	void atualizarAutor_ComIdAutorInvalido_RetornaNotFound() throws Exception {
		when(service.buscarOuFalhar(autorId)).thenThrow(new AutorNaoEncontradoException(autorId));
		
		mockMvc.perform(put("/autores/{autorId}", autorId)
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(autorInput)))
		.andExpect(status().isNotFound())
		.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(AutorNaoEncontradoException.class));
	}
	
	@Test
	@DisplayName("Dado um autorId valido Quando chamar metodo excluir Entao deve retornar status 204")
	void removerAutor_ComIdAutorValido_RetornaNoContent() throws Exception {
		mockMvc.perform(delete("/autores/{autorId}",autorId)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent())
		.andReturn();

		verify(service, Mockito.times(1)).excluir(anyLong());
		verifyNoMoreInteractions(service);
	} 
	
	@Test
	@DisplayName("Dado um autorId invalido Quando chamar metodo excluir Entao deve retornar status 404")
	void removerAutor_ComIdAutorInvalido_RetornaNotFound() throws Exception {
		Mockito.doThrow(AutorNaoEncontradoException.class).when(service).excluir(Mockito.anyLong());

		mockMvc.perform(delete("/autores/{autorId}",autorId)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(AutorNaoEncontradoException.class))
		.andReturn();

		verify(service, Mockito.times(1)).excluir(anyLong());
		verifyNoMoreInteractions(service);	
	} 
	
	@Test
	@DisplayName("Dado um autorId valido em uso Quando chamar metodo excluir Entao deve retornar status 409")
	void removerAutor_ComIdAutorEmUso_RetornaConflict() throws Exception {
		Mockito.doThrow(EntidadeEmUsoException.class).when(service).excluir(Mockito.anyLong());

		mockMvc.perform(delete("/autores/{autorId}",autorId)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isConflict())
		.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class))
		.andReturn();

		verify(service, Mockito.times(1)).excluir(anyLong());
		verifyNoMoreInteractions(service);	
	} 

}
