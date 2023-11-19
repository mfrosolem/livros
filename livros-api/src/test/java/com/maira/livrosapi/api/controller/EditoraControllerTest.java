package com.maira.livrosapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.api.assembler.EditoraInputDisassembler;
import com.maira.livrosapi.api.assembler.EditoraModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.EditoraModel;
import com.maira.livrosapi.api.model.input.EditoraInput;
import com.maira.livrosapi.domain.exception.EditoraNaoEncontradaException;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.service.EditoraService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {EditoraController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class EditoraControllerTest {

    @MockBean
    EditoraService service;

    @MockBean
    EditoraModelAssembler editoraModelAssembler;

    @MockBean
    EditoraInputDisassembler editoraInputDisassembler;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Editora editora;
    private Editora editoraSemId;
    private EditoraInput editoraInput;
    private EditoraModel editoraModel;
    private Long editoraId;

    @BeforeEach
    public void init() {

        editora = Editora.builder().id(1L).nome("Belas Letras").build();
        editoraId = 1L;
        editoraSemId = Editora.builder().nome("Belas Letras").build();
        editoraInput = EditoraInput.builder().nome("Belas Letras").build();
        editoraModel = EditoraModel.builder().id(1L).nome("Belas Letras").build();

    }

    @Test
    @DisplayName("Quando chamar GET Entao deve retornar status 200")
    void listarEditora_RetornaOK() throws Exception {
        when(service.listByContaining(anyString(), Mockito.any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Editora> pageEditora = new PageImpl<Editora>(Collections.singletonList(editora), pageableParametro, 1);
                    return pageEditora;
                });

        when(editoraModelAssembler.toCollectionModel(Mockito.anyList()))
                .thenReturn(Collections.singletonList(editoraModel));

        mockMvc.perform(get("/editoras"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando nome Entao deve retornar status 200")
    void listarEditora_PorNome_RetornaListaFiltradaEStatusOK() throws Exception {
        when(service.listByContaining(anyString(), Mockito.any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Editora> pageEditora = new PageImpl<Editora>(Collections.singletonList(editora), pageableParametro, 1);
                    return pageEditora;
                });

        when(editoraModelAssembler.toCollectionModel(Mockito.anyList()))
                .thenReturn(Collections.singletonList(editoraModel));

        mockMvc.perform(get("/editoras")
                        .param("nome", editora.getNome()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content[0].nome").value(editora.getNome()))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando nome inexistente Entao deve retornar lista vazia")
    void listarEditora_PorNomeInexistente_RetornaListaVaziaStatusOK() throws Exception {
        when(service.listByContaining(anyString(), Mockito.any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Editora> pageEditora = new PageImpl<Editora>(Collections.emptyList(), pageableParametro, 1);
                    return pageEditora;
                });

        when(editoraModelAssembler.toCollectionModel(Mockito.anyList()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/editoras")
                        .param("nome", editora.getNome()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.empty()))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando pageable Entao deve retornar status 200")
    void listarEditora_PassandoPageable_RetornaListaEStatusOK () throws Exception {

        when(service.listByContaining(anyString(), Mockito.any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Editora> pageEditora = new PageImpl<Editora>(Collections.singletonList(editora), pageableParametro, 1);
                    return pageEditora;
                });

        when(editoraModelAssembler.toCollectionModel(Mockito.anyList()))
                .thenReturn(Collections.singletonList(editoraModel));

        mockMvc.perform(get("/editoras")
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
    @DisplayName("Dado um editoraId valido Quando chamar GET Entao deve retornar o editora")
    void buscarEditora_ComEditoraIdValido_RetornaEditora () throws Exception {
        when(service.buscarOuFalhar(anyLong())).thenReturn(editora);
        when(editoraModelAssembler.toModel(Mockito.any(Editora.class))).thenReturn(editoraModel);

        mockMvc.perform(get("/editoras/{editoraId}", editoraId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(editoraModel.getId()))
                .andDo(print());

        verify(service, Mockito.times(1)).buscarOuFalhar(anyLong());
        verifyNoMoreInteractions(service);
    }


    @Test
    @DisplayName("Dado um editoraId invalido Quando chamar GET Entao deve retornar status 404")
    void buscarEditora_ComEditoraIdInvalido_RetornaNotFound() throws Exception {
        when(service.buscarOuFalhar(editoraId)).thenThrow(new EditoraNaoEncontradaException(editoraId));

        mockMvc.perform(get("/editoras/{editoraId}",editoraId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EditoraNaoEncontradaException.class));

        verify(service, Mockito.times(1)).buscarOuFalhar(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um editora valido Quando chamar POST Entao deve retornar status 201")
    void adicionarEditora_ComEditoraValido_RetornaCreated() throws Exception {
        when(editoraInputDisassembler.toDomainObject(Mockito.any(EditoraInput.class))).thenReturn(editoraSemId);
        when(service.salvar(Mockito.any(Editora.class))).thenReturn(editora);
        when(editoraModelAssembler.toModel(Mockito.any(Editora.class))).thenReturn(editoraModel);

        mockMvc.perform(post("/editoras")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editoraInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        verify(service, Mockito.times(1)).salvar(Mockito.any(Editora.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um editora com nome e sobrenome existente Quando chamar POST Entao deve retornar status 409")
    void adicionarEditora_ComNomeSobrenomeEditoraExistente_RetornaConflict() throws Exception {
        when(editoraInputDisassembler.toDomainObject(Mockito.any(EditoraInput.class))).thenReturn(editoraSemId);
        when(service.salvar(Mockito.any(Editora.class)))
                .thenThrow(new EntidadeEmUsoException(String.format("Editora de nome %s já cadastrada",
                        editoraSemId.getNome())));

        mockMvc.perform(post("/editoras")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editoraInput)))
                .andExpect(status().isConflict())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class));

        verify(service, Mockito.times(1)).salvar(Mockito.any(Editora.class));
        verifyNoMoreInteractions(service);
    }


    @Test
    @DisplayName("Dado um editora valido Quando chamar PUT Entao deve retornar status 200")
    void atualizarEditora_ComEditoraValido_RetornaOK() throws Exception {
        Mockito.when(service.buscarOuFalhar(anyLong())).thenReturn(editora);
        when(service.salvar(Mockito.any(Editora.class))).thenReturn(editora);
        when(editoraModelAssembler.toModel(Mockito.any(Editora.class))).thenReturn(editoraModel);

        mockMvc.perform(put("/editoras/{editoraId}", editoraId)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editoraInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(editoraId));
    }

    @Test
    @DisplayName("Dado um editoraId invalido Quando chamar PUT Entao deve retornar status 404")
    void atualizarEditora_ComIdEditoraInvalido_RetornaNotFound() throws Exception {
        when(service.buscarOuFalhar(editoraId)).thenThrow(new EditoraNaoEncontradaException(editoraId));

        mockMvc.perform(put("/editoras/{editoraId}", editoraId)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editoraInput)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EditoraNaoEncontradaException.class));
    }

    @Test
    @DisplayName("Dado um editoraId valido Quando chamar metodo excluir Entao deve retornar status 204")
    void removerEditora_ComIdEditoraValido_RetornaNoContent() throws Exception {
        mockMvc.perform(delete("/editoras/{editoraId}",editoraId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(service, Mockito.times(1)).excluir(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um editoraId invalido Quando chamar metodo excluir Entao deve retornar status 404")
    void removerEditora_ComIdEditoraInvalido_RetornaNotFound() throws Exception {
        Mockito.doThrow(EditoraNaoEncontradaException.class).when(service).excluir(Mockito.anyLong());

        mockMvc.perform(delete("/editoras/{editoraId}",editoraId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EditoraNaoEncontradaException.class))
                .andReturn();

        verify(service, Mockito.times(1)).excluir(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um editoraId valido em uso Quando chamar metodo excluir Entao deve retornar status 409")
    void removerEditora_ComIdEditoraEmUso_RetornaConflict() throws Exception {
        Mockito.doThrow(EntidadeEmUsoException.class).when(service).excluir(Mockito.anyLong());

        mockMvc.perform(delete("/editoras/{editoraId}",editoraId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class))
                .andReturn();

        verify(service, Mockito.times(1)).excluir(anyLong());
        verifyNoMoreInteractions(service);
    }

}