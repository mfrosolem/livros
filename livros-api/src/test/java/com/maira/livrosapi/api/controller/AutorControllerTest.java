package com.maira.livrosapi.api.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.api.assembler.AutorInputDisassembler;
import com.maira.livrosapi.api.assembler.AutorModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.AutorModel;
import com.maira.livrosapi.api.model.input.AutorInput;
import com.maira.livrosapi.domain.exception.AutorNaoEncontradoException;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.repository.AutorRepository;
import com.maira.livrosapi.domain.service.AutorService;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class AutorControllerTest {
	
	@InjectMocks
	AutorController controller;
	
	@Mock
	AutorService service;
	
	@Mock
	AutorRepository repository;
	
	@Mock
	AutorModelAssembler autorModelAssembler;

	@Mock
	AutorInputDisassembler autorInputDisassembler;
	
	MockMvc mockMvc;
	

	private Autor autor;
	private Autor autorSemId;
	private AutorInput autorInput;
	private AutorModel autorModel;
	private Long autorId;
	
	@BeforeEach
	public void init() {
		
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.alwaysDo(print())
				.setControllerAdvice(ApiExceptionHandler.class)
				.build();
		
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
	void Dado_um_autorId_valido_Quando_chamar_GET_Entao_deve_retornar_o_autor() throws Exception {
		when(service.buscarOuFalhar(anyLong())).thenReturn(autor);
		
		when(autorModelAssembler.toModel(Mockito.any(Autor.class))).thenReturn(autorModel);
		
		mockMvc.perform(get("/autores/{autorId}", autorId)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
		
		verify(service, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(service);		
	}
	
	
	@Test
	void Dado_um_autorId_invalido_Quando_chamar_GET_Entao_deve_retornar_status_404() throws Exception {
		when(service.buscarOuFalhar(autorId)).thenThrow(new AutorNaoEncontradoException(autorId));

		mockMvc.perform(get("/autores/{autorId}",autorId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof AutorNaoEncontradoException));
		
		verify(service, Mockito.times(1)).buscarOuFalhar(anyLong());
		verifyNoMoreInteractions(service);
	}
	
	@Test
	void Dado_um_autor_valido_Quando_chamar_POST_Entao_deve_retornar_status_201() throws Exception { 
		when(autorInputDisassembler.toDomainObject(Mockito.any(AutorInput.class))).thenReturn(autorSemId);

		when(service.salvar(Mockito.any(Autor.class))).thenReturn(autor);

		when(autorModelAssembler.toModel(Mockito.any(Autor.class))).thenReturn(autorModel);

		var objectMapper = new ObjectMapper();

		mockMvc.perform(post("/autores")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(autorInput)))
		.andExpect(status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

		verify(service, Mockito.times(1)).salvar(Mockito.any(Autor.class));
		verifyNoMoreInteractions(service);		
	}
	
	@Test
	void Dado_um_autorId_valido_Quando_chamar_metodo_excluir_Entao_deve_retornar_status_204() throws Exception {
		Mockito.doNothing().when(service).excluir(Mockito.anyLong());

		mockMvc.perform(delete("/autores/{autorId}",autorId)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isNoContent())
		.andReturn();

		verify(service, Mockito.times(1)).excluir(anyLong());
		verifyNoMoreInteractions(service);	
		
	} 

}
