package com.maira.livrosapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.api.assembler.UsuarioInputDisassembler;
import com.maira.livrosapi.api.assembler.UsuarioModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.UsuarioModel;
import com.maira.livrosapi.api.model.input.SenhaInput;
import com.maira.livrosapi.api.model.input.UsuarioComSenhaInput;
import com.maira.livrosapi.api.model.input.UsuarioInput;
import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.EntidadeNaoEncontradaException;
import com.maira.livrosapi.domain.exception.NegocioException;
import com.maira.livrosapi.domain.exception.UsuarioNaoEncontradoException;
import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.service.UsuarioService;
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
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.OffsetDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {UsuarioController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @MockBean
    UsuarioService service;

    @MockBean
    UsuarioModelAssembler usuarioModelAssembler;

    @MockBean
    UsuarioInputDisassembler usuarioInputDisassembler;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;
    private Usuario usuarioSemId;
    private UsuarioInput usuarioInput;
    private UsuarioComSenhaInput usuarioComSenhaInput;
    private UsuarioModel usuarioModel;
    private Long usuarioId;

    @BeforeEach
    public void init() {

        usuario = Usuario.builder().id(1L).nome("joao")
                .email("joao@livros")
                .senha("123")
                .dataCadastro(OffsetDateTime.now()).build();

        usuarioSemId = Usuario.builder().nome("joao")
                .email("joao@livros")
                .senha("123")
                .dataCadastro(OffsetDateTime.now()).build();

        usuarioComSenhaInput = UsuarioComSenhaInput.builder()
                .nome("joao")
                .email("joao@livros")
                .senha("123")
                .build();


        usuarioInput = UsuarioInput.builder().nome("joao")
                .email("joao@livros")
                .build();

        usuarioModel = UsuarioModel.builder().id(1L).nome("joao")
                .email("joao@livros")
                .build();

        usuarioId = 1L;

    }


    @Test
    @DisplayName("Quando chamar GET Entao deve retornar status 200")
    void listarUsuario_RetornaOK() throws Exception {
        when(service.listByNomeContaining(anyString(), Mockito.any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Usuario> pageUsuario = new PageImpl<Usuario>(Collections.singletonList(usuario), pageableParametro, 1);
                    return pageUsuario;
                });

        when(usuarioModelAssembler.toCollectionModel(Mockito.anyList()))
                .thenReturn(Collections.singletonList(usuarioModel));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando nome Entao deve retornar status 200")
    void listarUsuario_PorNome_RetornaListaFiltradaEStatusOK() throws Exception {
        when(service.listByNomeContaining(anyString(), Mockito.any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Usuario> pageUsuario = new PageImpl<Usuario>(Collections.singletonList(usuario), pageableParametro, 1);
                    return pageUsuario;
                });

        when(usuarioModelAssembler.toCollectionModel(Mockito.anyList()))
                .thenReturn(Collections.singletonList(usuarioModel));

        mockMvc.perform(get("/usuarios")
                        .param("nome", usuario.getNome()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content[0].nome").value(usuario.getNome()))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando nome inexistente Entao deve retornar lista vazia")
    void listarUsuario_PorNomeInexistente_RetornaListaVaziaStatusOK() throws Exception {
        when(service.listByNomeContaining(anyString(), Mockito.any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Usuario> pageUsuario = new PageImpl<Usuario>(Collections.emptyList(), pageableParametro, 1);
                    return pageUsuario;
                });

        when(usuarioModelAssembler.toCollectionModel(Mockito.anyList()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuarios")
                        .param("nome", usuario.getNome()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.empty()))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando pageable Entao deve retornar status 200")
    void listarUsuario_PassandoPageable_RetornaListaEStatusOK () throws Exception {

        when(service.listByNomeContaining(anyString(), Mockito.any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Usuario> pageUsuario = new PageImpl<Usuario>(Collections.singletonList(usuario), pageableParametro, 1);
                    return pageUsuario;
                });

        when(usuarioModelAssembler.toCollectionModel(Mockito.anyList()))
                .thenReturn(Collections.singletonList(usuarioModel));

        mockMvc.perform(get("/usuarios")
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
    @DisplayName("Dado um usuarioId valido Quando chamar GET Entao deve retornar o usuario")
    void buscarUsuario_ComUsuarioIdValido_RetornaUsuario () throws Exception {
        when(service.buscarOuFalhar(anyLong())).thenReturn(usuario);
        when(usuarioModelAssembler.toModel(Mockito.any(Usuario.class))).thenReturn(usuarioModel);

        mockMvc.perform(get("/usuarios/{usuarioId}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioModel.getId()))
                .andDo(print());

        verify(service, Mockito.times(1)).buscarOuFalhar(anyLong());
        verifyNoMoreInteractions(service);
    }


    @Test
    @DisplayName("Dado um usuarioId invalido Quando chamar GET Entao deve retornar status 404")
    void buscarUsuario_ComUsuarioIdInvalido_RetornaNotFound() throws Exception {
        when(service.buscarOuFalhar(usuarioId)).thenThrow(new UsuarioNaoEncontradoException(usuarioId));

        mockMvc.perform(get("/usuarios/{usuarioId}",usuarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertThat(result.getResolvedException()).isInstanceOf(UsuarioNaoEncontradoException.class))
                .andDo(print());

        verify(service, Mockito.times(1)).buscarOuFalhar(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um usuario valido Quando chamar POST Entao deve retornar status 201")
    void adicionarUsuario_ComUsuarioValido_RetornaCreated() throws Exception {
        when(usuarioInputDisassembler.toDomainObject(Mockito.any(UsuarioInput.class))).thenReturn(usuarioSemId);
        when(service.salvar(Mockito.any(Usuario.class))).thenReturn(usuario);
        when(usuarioModelAssembler.toModel(Mockito.any(Usuario.class))).thenReturn(usuarioModel);

        mockMvc.perform(post("/usuarios")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioComSenhaInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());

        verify(service, Mockito.times(1)).salvar(Mockito.any(Usuario.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um usuario com email existente Quando chamar POST Entao deve retornar status 409")
    void adicionarUsuario_ComEmailUsuarioExistente_RetornaConflict() throws Exception {
        when(usuarioInputDisassembler.toDomainObject(Mockito.any(UsuarioInput.class))).thenReturn(usuarioSemId);
        when(service.salvar(Mockito.any(Usuario.class)))
                .thenThrow(new EntidadeEmUsoException(String.format("Já existe um usuário cadastrado com o e-mail %s",
                        usuarioSemId.getEmail())));

        mockMvc.perform(post("/usuarios")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioComSenhaInput)))
                .andExpect(status().isConflict())
                .andExpect(result ->
                        assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class))
                .andDo(print());

        verify(service, Mockito.times(1)).salvar(Mockito.any(Usuario.class));
        verifyNoMoreInteractions(service);
    }


    @Test
    @DisplayName("Dado um usuario valido Quando chamar PUT Entao deve retornar status 200")
    void atualizarUsuario_ComUsuarioValido_RetornaOK() throws Exception {
        Mockito.when(service.buscarOuFalhar(anyLong())).thenReturn(usuario);
        when(service.salvar(Mockito.any(Usuario.class))).thenReturn(usuario);
        when(usuarioModelAssembler.toModel(Mockito.any(Usuario.class))).thenReturn(usuarioModel);

        mockMvc.perform(put("/usuarios/{usuarioId}", usuarioId)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(usuarioId))
                .andDo(print());
    }

    @Test
    @DisplayName("Dado um usuarioId invalido Quando chamar PUT Entao deve retornar status 404")
    void atualizarUsuario_ComIdUsuarioInvalido_RetornaNotFound() throws Exception {
        when(service.buscarOuFalhar(usuarioId)).thenThrow(new UsuarioNaoEncontradoException(usuarioId));

        mockMvc.perform(put("/usuarios/{usuarioId}", usuarioId)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioInput)))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertThat(result.getResolvedException()).isInstanceOf(UsuarioNaoEncontradoException.class))
                .andDo(print());
    }

    @Test
    @DisplayName("Dado um usuario com os dados validos Quando chamar metodo alterarSenha Entao deve retornar status 204")
    void alterarSenhaUsuario_ComDadosUsuarioValido_RetornaNoContent() throws Exception {
        var senhaInput = SenhaInput.builder().senhaAtual("123").novaSenha("321").build();

        mockMvc.perform(put("/usuarios/{usuarioId}/senha",usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(senhaInput)))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(service, Mockito.times(1)).alterarSenha(anyLong(), anyString(), anyString());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um usuario com a senha atual nao confere Quando chamar metodo alterarSenha Entao deve retornar status 400")
    void alterarSenhaUsuario_ComSenhaUsuarioNaoConfere_RetornaBadRequest() throws Exception {
        var senhaInput = SenhaInput.builder().senhaAtual("123").novaSenha("321").build();
        doThrow(new NegocioException("Senha atual informada não coincide com a senha do usuário."))
                .when(service).alterarSenha(anyLong(), anyString(), anyString());

        mockMvc.perform(put("/usuarios/{usuarioId}/senha",usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(senhaInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NegocioException.class))
                .andDo(print());

        verify(service, Mockito.times(1)).alterarSenha(anyLong(), anyString(), anyString());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um idUsuario inexistente Quando chamar metodo alterarSenha Entao deve retornar status 404")
    void alterarSenhaUsuario_ComIdUsuarioInexistente_RetornaNotFound() throws Exception {
        var senhaInput = SenhaInput.builder().senhaAtual("123").novaSenha("321").build();
        doThrow(new UsuarioNaoEncontradoException(usuarioId))
                .when(service).alterarSenha(anyLong(), anyString(), anyString());

        mockMvc.perform(put("/usuarios/{usuarioId}/senha",usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(senhaInput)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeNaoEncontradaException.class))
                .andDo(print());

        verify(service, Mockito.times(1)).alterarSenha(anyLong(), anyString(), anyString());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um usuario com senhaAtual ou novaSenha nao informada Quando chamar metodo alterarSenha Entao deve retornar status 400")
    void alterarSenhaUsuario_ComSenhaAtualOuNovaSenhaNaoInformada_RetornaBadRequest() throws Exception {
        var senhaInput = SenhaInput.builder().senhaAtual(null).novaSenha(null).build();
        doThrow(new UsuarioNaoEncontradoException(usuarioId))
                .when(service).alterarSenha(anyLong(), anyString(), anyString());

        mockMvc.perform(put("/usuarios/{usuarioId}/senha",usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(senhaInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
                .andDo(print());

        verify(service, never()).alterarSenha(anyLong(), anyString(), anyString());
        verifyNoMoreInteractions(service);
    }


}