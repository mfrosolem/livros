package com.maira.livrosapi.api.controller;

import com.maira.livrosapi.api.assembler.PermissaoModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.PermissaoModel;
import com.maira.livrosapi.domain.exception.GrupoNaoEncontradoException;
import com.maira.livrosapi.domain.exception.PermissaoNaoEncontradaException;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.service.GrupoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {GrupoPermissaoController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class GrupoPermissaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GrupoService grupoService;

    @MockBean
    private PermissaoModelAssembler permissaoModelAssembler;

    Grupo grupo;
    Long grupoId = 1L;

    Permissao permissaoConsultar;
    Permissao permissaoCadastrar;
    Permissao permissaoRemover;
    Long permissaoId = 1L;


    @BeforeEach
    void init() {
        permissaoConsultar = Permissao.builder()
                .id(1L)
                .nome("CONSULTAR")
                .descricao("Pode pesquisar")
                .build();
        permissaoCadastrar = Permissao.builder()
                .id(2L)
                .nome("CADASTRAR")
                .descricao("Pode cadastrar e editar")
                .build();

        permissaoRemover = Permissao.builder()
                .id(3L)
                .nome("REMOVER")
                .descricao("Pode remover")
                .build();

        grupo = Grupo.builder().nome("GRP_CADASTRO")
                .permissoes(Set.of(permissaoConsultar, permissaoCadastrar)).build();


    }

    @Test
    @DisplayName("Quando chamar GET Com grupoId existente Entao deve retornar status 200 e lista de permissoes")
    void listarGrupoPermissao_ComGrupoIdExistente_RetornaOK() throws Exception {
        when(grupoService.buscarOuFalhar(anyLong())).thenReturn(grupo);
        List<PermissaoModel> permissoesModel = new ArrayList<>() {
            {
                add(PermissaoModel.builder().id(1L).nome("CONSULTAR").descricao("Pode pesquisar").build());
                add(PermissaoModel.builder().id(2L).nome("CADASTRAR").descricao("Pode cadastrar e editar").build());
            }
        };
        when(permissaoModelAssembler.toCollectionModel(anyList())).thenReturn(permissoesModel);

        mockMvc.perform(get("/grupos/{grupoId}/permissoes", grupoId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET Com grupoId inexistente Entao deve retornar status 404")
    void listarGrupoPermissao_ComGrupoIdInexistente_RetornaNotFound() throws Exception {
        when(grupoService.buscarOuFalhar(anyLong())).thenThrow(new GrupoNaoEncontradoException(grupoId));

        mockMvc.perform(get("/grupos/{grupoId}/permissoes", grupoId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(GrupoNaoEncontradoException.class))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar PUT Com grupoId e permissaoId existentes Entao deve retornar status 204")
    void associarGrupoPermissao_ComDadosExistentes_RetornaNoContent() throws Exception {
        mockMvc.perform(put("/grupos/{grupoId}/permissoes/{permissaoId}", grupoId, permissaoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar PUT Com grupoId inexistente e permissaoId existente Entao deve retornar status 404")
    void associarGrupoPermissao_ComGrupoIdInexistente_RetornaNotFound() throws Exception {
        doThrow(new GrupoNaoEncontradoException(grupoId)).when(grupoService).associarPermissao(anyLong(), anyLong());

        mockMvc.perform(put("/grupos/{grupoId}/permissoes/{permissaoId}", grupoId, permissaoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(GrupoNaoEncontradoException.class))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar PUT Com grupoId existente e permissaoId inexistente Entao deve retornar status 404")
    void associarGrupoPermissao_ComPermissaoIdInexistente_RetornaNotFound() throws Exception {
        doThrow(new PermissaoNaoEncontradaException(permissaoRemover.getId())).when(grupoService)
                .associarPermissao(anyLong(), anyLong());

        mockMvc.perform(put("/grupos/{grupoId}/permissoes/{permissaoId}", grupoId, permissaoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(PermissaoNaoEncontradaException.class))
                .andDo(print());
    }


    @Test
    @DisplayName("Quando chamar DELETE Com grupoId e permissaoId existentes Entao deve retornar status 204")
    void desassociarGrupoPermissao_ComDadosExistentes_RetornaNoContent() throws Exception {
        mockMvc.perform(delete("/grupos/{grupoId}/permissoes/{permissaoId}", grupoId, permissaoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar DELETE Com grupoId inexistente e permissaoId existente Entao deve retornar status 404")
    void desassociarGrupoPermissao_ComGrupoIdInexistente_RetornaNotFound() throws Exception {
        doThrow(new GrupoNaoEncontradoException(grupoId)).when(grupoService).desassociarPermissao(anyLong(), anyLong());

        mockMvc.perform(delete("/grupos/{grupoId}/permissoes/{permissaoId}", grupoId, permissaoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(GrupoNaoEncontradoException.class))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar DELETE Com grupoId existente e permissaoId inexistente Entao deve retornar status 404")
    void desassociarGrupoPermissao_ComPermissaoIdInexistente_RetornaNotFound() throws Exception {
        doThrow(new PermissaoNaoEncontradaException(permissaoRemover.getId())).when(grupoService)
                .desassociarPermissao(anyLong(), anyLong());

        mockMvc.perform(delete("/grupos/{grupoId}/permissoes/{permissaoId}", grupoId, permissaoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(PermissaoNaoEncontradaException.class))
                .andDo(print());
    }

}