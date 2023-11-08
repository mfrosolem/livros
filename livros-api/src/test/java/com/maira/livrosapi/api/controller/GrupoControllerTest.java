package com.maira.livrosapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.api.assembler.GrupoInputDisassembler;
import com.maira.livrosapi.api.assembler.GrupoModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.GrupoModel;
import com.maira.livrosapi.api.model.input.GrupoInput;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GrupoNaoEncontradoException;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.service.GrupoService;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@ContextConfiguration(classes = {GrupoController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class GrupoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GrupoService service;

    @MockBean
    private GrupoInputDisassembler grupoInputDisassembler;

    @MockBean
    private GrupoModelAssembler grupoModelAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Grupo grupo;
    private Grupo grupoSemId;
    private GrupoInput grupoInput;
    private GrupoModel grupoModel;
    private Long grupoId;

    @BeforeEach
    public void init() {

        grupoId = 1L;
        grupoInput = GrupoInput.builder().nome("Visitante").build();
        grupoSemId = Grupo.builder().nome("Visitante").build();
        grupo = Grupo.builder().id(1L).nome("Visitante").permissoes(Set.of(Permissao.builder().nome("CONSULTAR").descricao("Consultar").build())).build();
        grupoModel = GrupoModel.builder().id(1L).nome("Visitante").build();

    }

    @Test
    @DisplayName("Quando chamar GET Entao deve retornar status 200")
    void listarGrupo_RetornaOK() throws Exception {

        when(service.listyByNameContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Grupo> pageGrupo = new PageImpl<Grupo>(Collections.singletonList(grupo), pageableParametro, 1);
                    return pageGrupo;
                });

        when(grupoModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.singletonList(grupoModel));

        mockMvc.perform(get("/grupos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando nome Entao deve retornar status 200")
    void listarGrupo_PorNome_RetornaListaFiltradaEStatusOK() throws Exception {
        when(service.listyByNameContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Grupo> pageGrupo = new PageImpl<Grupo>(Collections.singletonList(grupo), pageableParametro, 1);
                    return pageGrupo;
                });

        when(grupoModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.singletonList(grupoModel));

        mockMvc.perform(get("/grupos").param("nome", grupo.getNome()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando nome inexistente Entao deve retornar lista vazia")
    void listarGrupo_PorNomeInexistente_RetornaListaVaziaStatusOK() throws Exception {
        when(service.listyByNameContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Grupo> pageGrupo = new PageImpl<Grupo>(Collections.emptyList(), pageableParametro, 1);
                    return pageGrupo;
                });

        when(grupoModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/grupos").param("nome", grupo.getNome()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.empty()))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando pageable Entao deve retornar status 200")
    void listarGrupo_PassandoPageable_RetornaListaStatusOK() throws Exception {
        when(service.listyByNameContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Grupo> pageGrupo = new PageImpl<Grupo>(Collections.singletonList(grupo), pageableParametro, 1);
                    return pageGrupo;
                });

        when(grupoModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.singletonList(grupoModel));

        mockMvc.perform(get("/grupos")
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
    @DisplayName("Dado um grupoId valido Quando chamar GET Entao deve retornar o grupo")
    void buscarGrupo_ComGrupoIdValido_RetornaGrupo() throws Exception {
        when(service.buscarOuFalhar(anyLong())).thenReturn(grupo);
        when(grupoModelAssembler.toModel(any(Grupo.class))).thenReturn(grupoModel);

        mockMvc
                .perform(get("/grupos/{grupoId}", grupoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Dado um grupoId invalido Quando chamar GET Entao deve retornar status 404")
    void buscarGrupo_ComGrupoIdInvalido_RetornaNotFound() throws Exception {
        when(service.buscarOuFalhar(grupoId)).thenThrow(new GrupoNaoEncontradoException(grupoId));

        mockMvc.perform(get("/grupos/{grupoId}",grupoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(GrupoNaoEncontradoException.class));
    }

    @Test
    @DisplayName("Dado um grupo valido Quando chamar POST Entao deve retornar status 201")
    void adicionarGrupo_ComDadosValidos_RetornaCreated() throws Exception {
        when(grupoInputDisassembler.toDomainObject(any(GrupoInput.class))).thenReturn(grupoSemId);
        when(service.salvar(any(Grupo.class))).thenReturn(grupo);
        when(grupoModelAssembler.toModel(any(Grupo.class))).thenReturn(grupoModel);

        mockMvc.perform(post("/grupos")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(grupoInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        verify(service, times(1)).salvar(any(Grupo.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um grupo com nome existente Quando chamar POST Entao deve retornar status 409")
    void adicionarGrupo_ComNomeGrupoExistente_RetornaConflict() throws Exception {
        when(grupoInputDisassembler.toDomainObject(Mockito.any(GrupoInput.class))).thenReturn(grupoSemId);
        when(service.salvar(Mockito.any(Grupo.class)))
                .thenThrow(new EntidadeEmUsoException(String.format("Grupo de descrição %s já cadastrado",
                        grupoSemId.getNome())));

        mockMvc.perform(post("/grupos")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(grupoInput)))
                .andExpect(status().isConflict())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class));

        verify(service, Mockito.times(1)).salvar(Mockito.any(Grupo.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um grupo valido Quando chamar PUT Entao deve retornar status 200")
    void atualizarGrupo_ComGrupoValido_RetornarOK() throws Exception {
        when(service.buscarOuFalhar(anyLong())).thenReturn(grupo);
        when(service.salvar(any(Grupo.class))).thenReturn(grupo);
        when(grupoModelAssembler.toModel(any(Grupo.class))).thenReturn(grupoModel);

        mockMvc.perform(put("/grupos/{grupoId}", grupoId)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(grupoInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(grupoId));

        verify(service, times(1)).salvar(any(Grupo.class));
    }


    @Test
    @DisplayName("Dado um grupoId invalido Quando chamar PUT Entao deve retornar status 404")
    void atualizarGrupo_ComGrupoIdInvalido_RetornarNotFound() throws Exception {
        when(service.buscarOuFalhar(grupoId)).thenThrow(new GrupoNaoEncontradoException(grupoId));

        mockMvc.perform(put("/grupos/{grupoId}", grupoId)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(grupoInput)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(GrupoNaoEncontradoException.class));

        verify(service, never()).salvar(any(Grupo.class));
    }

    @Test
    @DisplayName("Dado um grupoId valido Quando chamar metodo excluir Entao deve retornar status 204")
    void removerGrupo_ComGrupoIdValido_RetornarNoContent() throws Exception {
        mockMvc.perform(delete("/grupos/{grupoId}",grupoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(service, times(1)).excluir(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um grupoId invalido Quando chamar metodo excluir Entao deve retornar status 404")
    void removerGrupo_ComGrupoIdInvalido_RetornarNotFound() throws Exception {
        doThrow(new GrupoNaoEncontradoException(grupoId)).when(service).excluir(grupoId);

        mockMvc.perform(delete("/grupos/{grupoId}",grupoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Dado um grupoId valido em uso Quando chamar metodo excluir Entao deve retornar status 409")
    void removerGrupo_ComGrupoIdEmUso_RetornarConfict() throws Exception {
        doThrow(EntidadeEmUsoException.class).when(service).excluir(anyLong());

        mockMvc.perform(delete("/grupos/{grupoId}",grupoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class))
                .andReturn();

        verify(service, times(1)).excluir(anyLong());
        verifyNoMoreInteractions(service);
    }

}