package com.maira.livrosapi.api.controller;

import com.maira.livrosapi.api.assembler.GrupoModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.GrupoModel;
import com.maira.livrosapi.domain.exception.GrupoNaoEncontradoException;
import com.maira.livrosapi.domain.exception.UsuarioNaoEncontradoException;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.service.UsuarioService;
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
@ContextConfiguration(classes = {UsuarioGrupoController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class UsuarioGrupoControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private GrupoModelAssembler grupoModelAssembler;

    Usuario usuario;
    Long usuarioId = 1L;

    Grupo grupoConsultar;
    Grupo grupoCadastrar;
    Grupo grupoRemover;
    Long grupoId = 1L;


    @BeforeEach
    void init() {
        grupoConsultar = Grupo.builder()
                .id(1L)
                .nome("CONSULTAR")
                .build();
        grupoCadastrar = Grupo.builder()
                .id(2L)
                .nome("CADASTRAR")
                .build();

        grupoRemover = Grupo.builder()
                .id(3L)
                .nome("REMOVER")
                .build();

        usuario = Usuario.builder().nome("Teste").email("teste@livros.com").senha("123456")
                .grupos(Set.of(grupoConsultar, grupoCadastrar)).build();


    }

    @Test
    @DisplayName("Quando chamar GET Com usuarioId existente Entao deve retornar status 200 e lista de permissoes")
    void listarUsuarioGrupo_ComUsuarioIdExistente_RetornaOK() throws Exception {
        when(usuarioService.buscarOuFalhar(anyLong())).thenReturn(usuario);
        List<GrupoModel> gruposModel = new ArrayList<>() {
            {
                add(GrupoModel.builder().id(1L).nome("CONSULTAR").build());
                add(GrupoModel.builder().id(2L).nome("CADASTRAR").build());
            }
        };
        when(grupoModelAssembler.toCollectionModel(anyList())).thenReturn(gruposModel);

        mockMvc.perform(get("/usuarios/{usuarioId}/grupos", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET Com usuarioId inexistente Entao deve retornar status 404")
    void listarUsuarioGrupo_ComUsuarioIdInexistente_RetornaNotFound() throws Exception {
        when(usuarioService.buscarOuFalhar(anyLong())).thenThrow(new UsuarioNaoEncontradoException(usuarioId));

        mockMvc.perform(get("/usuarios/{usuarioId}/grupos", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UsuarioNaoEncontradoException.class))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar PUT Com usuarioId e grupoId existentes Entao deve retornar status 204")
    void associarUsuarioGrupo_ComDadosExistentes_RetornaNoContent() throws Exception {
        mockMvc.perform(put("/usuarios/{usuarioId}/grupos/{grupoId}", usuarioId, grupoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar PUT Com usuarioId inexistente e grupoId existente Entao deve retornar status 404")
    void associarUsuarioGrupo_ComUsuarioIdInexistente_RetornaNotFound() throws Exception {
        doThrow(new UsuarioNaoEncontradoException(usuarioId)).when(usuarioService).associarGrupo(anyLong(), anyLong());

        mockMvc.perform(put("/usuarios/{usuarioId}/grupos/{grupoId}", usuarioId, grupoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UsuarioNaoEncontradoException.class))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar PUT Com usuarioId existente e grupoId inexistente Entao deve retornar status 404")
    void associarUsuarioGrupo_ComPermissaoIdInexistente_RetornaNotFound() throws Exception {
        doThrow(new GrupoNaoEncontradoException(grupoRemover.getId())).when(usuarioService)
                .associarGrupo(anyLong(), anyLong());

        mockMvc.perform(put("/usuarios/{usuarioId}/grupos/{grupoId}", usuarioId, grupoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(GrupoNaoEncontradoException.class))
                .andDo(print());
    }


    @Test
    @DisplayName("Quando chamar DELETE Com usuarioId e grupoId existentes Entao deve retornar status 204")
    void desassociarUsuarioGrupo_ComDadosExistentes_RetornaNoContent() throws Exception {
        mockMvc.perform(delete("/usuarios/{usuarioId}/grupos/{grupoId}", usuarioId, grupoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar DELETE Com usuarioId inexistente e grupoId existente Entao deve retornar status 404")
    void desassociarUsuarioGrupo_ComGrupoIdInexistente_RetornaNotFound() throws Exception {
        doThrow(new UsuarioNaoEncontradoException(usuarioId)).when(usuarioService).desassociarGrupo(anyLong(), anyLong());

        mockMvc.perform(delete("/usuarios/{usuarioId}/grupos/{grupoId}", usuarioId, grupoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UsuarioNaoEncontradoException.class))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar DELETE Com usuarioId existente e grupoId inexistente Entao deve retornar status 404")
    void desassociarUsuarioGrupo_ComPermissaoIdInexistente_RetornaNotFound() throws Exception {
        doThrow(new GrupoNaoEncontradoException(grupoRemover.getId())).when(usuarioService)
                .desassociarGrupo(anyLong(), anyLong());

        mockMvc.perform(delete("/usuarios/{usuarioId}/grupos/{grupoId}", usuarioId, grupoRemover.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(GrupoNaoEncontradoException.class))
                .andDo(print());
    }


}