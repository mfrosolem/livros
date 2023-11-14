package com.maira.livrosapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.api.assembler.PermissaoInputDisassembler;
import com.maira.livrosapi.api.assembler.PermissaoModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.PermissaoModel;
import com.maira.livrosapi.api.model.input.PermissaoInput;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.PermissaoNaoEncontradaException;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.service.PermissaoService;
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
@ContextConfiguration(classes = {PermissaoController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class PermissaoControllerTest {

    @MockBean
    PermissaoService service;

    @MockBean
    PermissaoModelAssembler permissaoModelAssembler;

    @MockBean
    PermissaoInputDisassembler permissaoInputDisassembler;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Permissao permissao;
    private PermissaoModel permissaoModel;
    private PermissaoInput permissaoInput;
    private Permissao permissaoSemId;
    private Long permissaoId;

    @BeforeEach
    public void init() {

        permissaoId = 1L;
        permissaoInput = PermissaoInput.builder().nome("ROLE_LIVRO_PESQUISAR").descricao("Permite pesquisar livros").build();
        permissaoSemId = Permissao.builder().nome("ROLE_LIVRO_PESQUISAR").descricao("Permite pesquisar livros").build();
        permissao = Permissao.builder().id(1L).nome("ROLE_LIVRO_PESQUISAR").descricao("Permite pesquisar livros").build();
        permissaoModel = PermissaoModel.builder().id(1L).nome("ROLE_LIVRO_PESQUISAR").descricao("Permite pesquisar livros").build();

    }


    @Test
    @DisplayName("Quando chamar GET Entao deve retornar status 200")
    void listarGernero_RetornaOK() throws Exception {

        when(service.listByNameContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Permissao> pagePermissao = new PageImpl<Permissao>(Collections.singletonList(permissao), pageableParametro, 1);
                    return pagePermissao;
                });

        when(permissaoModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.singletonList(permissaoModel));

        mockMvc.perform(get("/permissoes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando descricao Entao deve retornar status 200")
    void listarPermissao_PorDescricao_RetornaListaFiltradaEStatusOK() throws Exception {
        when(service.listByNameContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Permissao> pagePermissao = new PageImpl<Permissao>(Collections.singletonList(permissao), pageableParametro, 1);
                    return pagePermissao;
                });

        when(permissaoModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.singletonList(permissaoModel));

        mockMvc.perform(get("/permissoes")
                        .param("descricao", permissao.getDescricao()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content[0].descricao").value(permissao.getDescricao()))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando descricao inexistente Entao deve retornar lista vazia")
    void listarPermissao_PorDescricaoInexistente_RetornaListaVaziaStatusOK() throws Exception {
        when(service.listByNameContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Permissao> pagePermissao = new PageImpl<Permissao>(Collections.emptyList(), pageableParametro, 1);
                    return pagePermissao;
                });

        when(permissaoModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/permissoes")
                        .param("descricao", permissao.getDescricao()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.empty()))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando pageable Entao deve retornar status 200")
    void listarPermissao_PassandoPageable_RetornaListaEStatusOK() throws Exception {

        when(service.listByNameContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Permissao> pagePermissao = new PageImpl<Permissao>(Collections.singletonList(permissao), pageableParametro, 1);
                    return pagePermissao;
                });

        when(permissaoModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.singletonList(permissaoModel));

        mockMvc.perform(get("/permissoes")
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
    @DisplayName("Dado um permissaoId valido Quando chamar GET Entao deve retornar o permissao")
    void buscarPermissao_ComPermissaoIdValido_RetornaPermissao() throws Exception {
        when(service.buscarOuFalhar(anyLong())).thenReturn(permissao);

        when(permissaoModelAssembler.toModel(any(Permissao.class)))
                .thenAnswer(answer -> {
                    return permissaoModel;
                });

        mockMvc.perform(get("/permissoes/{permissaoId}",permissaoId))
                .andExpect(status().isOk())
                .andDo(print());

        verify(service, times(1)).buscarOuFalhar(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um permissaoId invalido Quando chamar GET Entao deve retornar status 404")
    void buscarPermissao_ComPermissaoIdInValido_RetornaNotFound() throws Exception {
        when(service.buscarOuFalhar(permissaoId)).thenThrow(new PermissaoNaoEncontradaException(permissaoId));

        mockMvc.perform(get("/permissoes/{permissaoId}",permissaoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(PermissaoNaoEncontradaException.class));

        verify(service, times(1)).buscarOuFalhar(anyLong());
        verifyNoMoreInteractions(service);
    }


    @Test
    @DisplayName("Dado um permissao valido Quando chamar POST Entao deve retornar status 201")
    void adicionarPermissao_ComDadosValidos_RetornaCreated() throws Exception {
        when(permissaoInputDisassembler.toDomainObject(any(PermissaoInput.class))).thenReturn(permissaoSemId);
        when(service.salvar(any(Permissao.class))).thenReturn(permissao);
        when(permissaoModelAssembler.toModel(any(Permissao.class))).thenReturn(permissaoModel);

        mockMvc.perform(post("/permissoes")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(permissaoInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        verify(service, times(1)).salvar(any(Permissao.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um permissao com descricao existente Quando chamar POST Entao deve retornar status 409")
    void adicionarPermissao_ComDescricaoPermissaoExistente_RetornaConflict() throws Exception {
        when(permissaoInputDisassembler.toDomainObject(Mockito.any(PermissaoInput.class))).thenReturn(permissaoSemId);
        when(service.salvar(Mockito.any(Permissao.class)))
                .thenThrow(new EntidadeEmUsoException(String.format("Permissao de descrição %s já cadastrado",
                        permissaoSemId.getDescricao())));

        mockMvc.perform(post("/permissoes")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(permissaoInput)))
                .andExpect(status().isConflict())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class));

        verify(service, Mockito.times(1)).salvar(Mockito.any(Permissao.class));
        verifyNoMoreInteractions(service);
    }


    @Test
    @DisplayName("Dado um permissao valido Quando chamar PUT Entao deve retornar status 200")
    void atualizarPermissao_ComPermissaoValido_RetornarOK() throws Exception {
        when(service.buscarOuFalhar(anyLong())).thenReturn(permissao);
        when(service.salvar(any(Permissao.class))).thenReturn(permissao);
        when(permissaoModelAssembler.toModel(any(Permissao.class))).thenReturn(permissaoModel);

        mockMvc.perform(put("/permissoes/{permissaoId}", permissaoId)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(permissaoInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(permissaoId));

        verify(service, times(1)).salvar(any(Permissao.class));
    }


    @Test
    @DisplayName("Dado um permissaoId invalido Quando chamar PUT Entao deve retornar status 404")
    void atualizarPermissao_ComPermissaoIdInvalido_RetornarNotFound() throws Exception {
        when(service.buscarOuFalhar(permissaoId)).thenThrow(new PermissaoNaoEncontradaException(permissaoId));

        mockMvc.perform(put("/permissoes/{permissaoId}", permissaoId)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(permissaoInput)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(PermissaoNaoEncontradaException.class));

        verify(service, never()).salvar(any(Permissao.class));
    }

    @Test
    @DisplayName("Dado um permissaoId valido Quando chamar metodo excluir Entao deve retornar status 204")
    void removerPermissao_ComPermissaoIdValido_RetornarNoContent() throws Exception {
        mockMvc.perform(delete("/permissoes/{permissaoId}",permissaoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(service, times(1)).excluir(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um permissaoId invalido Quando chamar metodo excluir Entao deve retornar status 404")
    void removerPermissao_ComPermissaoIdInvalido_RetornarNotFound() throws Exception {
        doThrow(new PermissaoNaoEncontradaException(permissaoId)).when(service).excluir(permissaoId);

        mockMvc.perform(delete("/permissoes/{permissaoId}",permissaoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Dado um permissaoId valido em uso Quando chamar metodo excluir Entao deve retornar status 409")
    void removerPermissao_ComPermissaoIdEmUso_RetornarConfict() throws Exception {
        doThrow(EntidadeEmUsoException.class).when(service).excluir(anyLong());

        mockMvc.perform(delete("/permissoes/{permissaoId}",permissaoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class))
                .andReturn();

        verify(service, times(1)).excluir(anyLong());
        verifyNoMoreInteractions(service);
    }

}