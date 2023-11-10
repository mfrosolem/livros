package com.maira.livrosapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.api.assembler.LivroInputDisassembler;
import com.maira.livrosapi.api.assembler.LivroModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.AutorModel;
import com.maira.livrosapi.api.model.EditoraModel;
import com.maira.livrosapi.api.model.GeneroModel;
import com.maira.livrosapi.api.model.LivroModel;
import com.maira.livrosapi.api.model.input.AutorIdInput;
import com.maira.livrosapi.api.model.input.EditoraIdInput;
import com.maira.livrosapi.api.model.input.GeneroIdInput;
import com.maira.livrosapi.api.model.input.LivroInput;
import com.maira.livrosapi.domain.exception.*;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.model.Livro;
import com.maira.livrosapi.domain.service.LivroService;
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

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {LivroController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class LivroControllerTest {

    @MockBean
    LivroService service;

    @MockBean
    LivroModelAssembler livroModelAssembler;

    @MockBean
    LivroInputDisassembler livroInputDisassembler;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Livro livro;
    private LivroModel livroModel;
    private LivroInput livroInput;
    private Livro livroSemId;
    private Long livroId = 1L;


    private Long generoId = 1L;
    private Long editoraId = 1L;
    private Long autorId = 1L;

    @BeforeEach
    public void init() {

        var genero = Genero.builder().id(generoId).descricao("Romance").build();
        var editora = Editora.builder().id(editoraId).nome("Penguin & Companhia das Letras").build();
        var autor = Autor.builder().id(autorId).nome("Joaquim Maria").sobrenome("Machado de Assis")
                .nomeConhecido("Machado de Assis").sexo("M").build();

        var generoModel = GeneroModel.builder().id(generoId).descricao("Romance").build();
        var editoraModel = EditoraModel.builder().id(editoraId).nome("Penguin & Companhia das Letras").build();
        var autorModel = AutorModel.builder().id(autorId).nome("Joaquim Maria").sobrenome("Machado de Assis")
                .nomeConhecido("Machado de Assis").sexo("M").build();

        livroInput = LivroInput.builder().isbn("9788582850350").titulo("Dom Casmurro").idioma("Português")
                .paginas(390L).ano(2016L)
                .genero(GeneroIdInput.builder().id(generoId).build())
                .autor(AutorIdInput.builder().id(autorId).build())
                .editora(EditoraIdInput.builder().id(editoraId).build())
                .build();

        livroSemId = Livro.builder().isbn("9788582850350").titulo("Dom Casmurro").idioma("Português")
                .paginas(390L).ano(2016L).genero(genero).autor(autor).editora(editora).build();

        livro = Livro.builder().id(livroId).isbn("9788582850350").titulo("Dom Casmurro").idioma("Português")
                .paginas(390L).ano(2016L).genero(genero).autor(autor).editora(editora).build();

        livroModel = LivroModel.builder().id(1L).isbn("9788582850350").titulo("Dom Casmurro").idioma("Português")
                .paginas(390L).ano(2016L).genero(generoModel).autor(autorModel).editora(editoraModel).build();
    }


    @Test
    @DisplayName("Quando chamar GET Entao deve retornar status 200")
    void listarGernero_RetornaOK() throws Exception {

        when(service.listByTituloContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Livro> pageLivro = new PageImpl<Livro>(Collections.singletonList(livro), pageableParametro, 1);
                    return pageLivro;
                });

        when(livroModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.singletonList(livroModel));

        mockMvc.perform(get("/livros"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando titulo Entao deve retornar status 200")
    void listarLivro_PorTitulo_RetornaListaFiltradaEStatusOK() throws Exception {
        when(service.listByTituloContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Livro> pageLivro = new PageImpl<Livro>(Collections.singletonList(livro), pageableParametro, 1);
                    return pageLivro;
                });

        when(livroModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.singletonList(livroModel));

        mockMvc.perform(get("/livros")
                        .param("titulo", livro.getTitulo()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content[0].titulo").value(livro.getTitulo()))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando titulo inexistente Entao deve retornar lista vazia")
    void listarLivro_PorTituloInexistente_RetornaListaVaziaStatusOK() throws Exception {
        when(service.listByTituloContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Livro> pageLivro = new PageImpl<Livro>(Collections.emptyList(), pageableParametro, 1);
                    return pageLivro;
                });

        when(livroModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/livros")
                        .param("titulo", livro.getTitulo()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.empty()))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando pageable Entao deve retornar status 200")
    void listarLivro_PassandoPageable_RetornaListaEStatusOK() throws Exception {

        when(service.listByTituloContaining(anyString(), any(Pageable.class)))
                .thenAnswer(answer -> {
                    Pageable pageableParametro = answer.getArgument(1, Pageable.class);
                    Page<Livro> pageLivro = new PageImpl<Livro>(Collections.singletonList(livro), pageableParametro, 1);
                    return pageLivro;
                });

        when(livroModelAssembler.toCollectionModel(anyList()))
                .thenReturn(Collections.singletonList(livroModel));

        mockMvc.perform(get("/livros")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "titulo,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$['pageable']['paged']").value("true"))
                .andExpect(jsonPath("$['sort']['sorted']").value("true"))
                .andDo(print());
    }


    @Test
    @DisplayName("Dado um livroId valido Quando chamar GET Entao deve retornar o livro")
    void buscarLivro_ComLivroIdValido_RetornaLivro() throws Exception {
        when(service.buscarOuFalhar(anyLong())).thenReturn(livro);

        when(livroModelAssembler.toModel(any(Livro.class)))
                .thenAnswer(answer -> {
                    return livroModel;
                });

        mockMvc.perform(get("/livros/{livroId}",livroId))
                .andExpect(status().isOk())
                .andDo(print());

        verify(service, times(1)).buscarOuFalhar(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um livroId invalido Quando chamar GET Entao deve retornar status 404")
    void buscarLivro_ComLivroIdInValido_RetornaNotFound() throws Exception {
        when(service.buscarOuFalhar(livroId)).thenThrow(new LivroNaoEncontradoException(livroId));

        mockMvc.perform(get("/livros/{livroId}",livroId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(LivroNaoEncontradoException.class));

        verify(service, times(1)).buscarOuFalhar(anyLong());
        verifyNoMoreInteractions(service);
    }


    @Test
    @DisplayName("Dado um livro valido Quando chamar POST Entao deve retornar status 201")
    void adicionarLivro_ComDadosValidos_RetornaCreated() throws Exception {

        when(livroInputDisassembler.toDomainObject(any(LivroInput.class))).thenReturn(livroSemId);
        when(service.salvar(any(Livro.class))).thenReturn(livro);
        when(livroModelAssembler.toModel(any(Livro.class))).thenReturn(livroModel);

        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        verify(service, times(1)).salvar(any(Livro.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um livro com titulo existente Quando chamar POST Entao deve retornar status 409")
    void adicionarLivro_ComTituloLivroExistente_RetornaConflict() throws Exception {
        when(livroInputDisassembler.toDomainObject(Mockito.any(LivroInput.class))).thenReturn(livroSemId);
        when(service.salvar(Mockito.any(Livro.class)))
                .thenThrow(new EntidadeEmUsoException(String.format("Livro de título %s já cadastrado",
                        livroSemId.getTitulo())));

        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroInput)))
                .andExpect(status().isConflict())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class))
                .andDo(print());

        verify(service, Mockito.times(1)).salvar(Mockito.any(Livro.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um livro com autorIdInput nao informado Quando chamar POST Entao deve retornar status 400")
    void adicionarLivro_ComAutorIdInputNaoInformado_RetornaBadRequest() throws Exception {
        livroInput.setAutor(null);

        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
                .andDo(print());
    }

    @Test
    @DisplayName("Dado um livro com generoIdInput nao informado Quando chamar POST Entao deve retornar status 400")
    void adicionarLivro_ComGeneroIdInputNaoInformado_RetornaBadRequest() throws Exception {
        livroInput.setGenero(null);

        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
                .andDo(print());
    }

    @Test
    @DisplayName("Dado um livro com editoraIdInput nao informado Quando chamar POST Entao deve retornar status 400")
    void adicionarLivro_ComEditoraIdInputNaoInformado_RetornaBadRequest() throws Exception {
        livroInput.setEditora(null);

        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
                .andDo(print());
    }

    @Test
    @DisplayName("Dado um livro com idAutor nao cadastrado Quando chamar POST Entao deve retornar status 404")
    void adicionarLivro_ComIdAutorNaoCadastrado_RetornaNotFound() throws Exception {
        when(livroInputDisassembler.toDomainObject(Mockito.any(LivroInput.class))).thenReturn(livroSemId);
        when(service.salvar(Mockito.any(Livro.class)))
                .thenThrow(new AutorNaoEncontradoException(livroSemId.getAutor().getId()));

        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroInput)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeNaoEncontradaException.class))
                .andDo(print());

        verify(service, Mockito.times(1)).salvar(Mockito.any(Livro.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um livro com idGenero nao cadastrado Quando chamar POST Entao deve retornar status 404")
    void adicionarLivro_ComIdGeneroNaoCadastrado_RetornaNotFound() throws Exception {
        when(livroInputDisassembler.toDomainObject(Mockito.any(LivroInput.class))).thenReturn(livroSemId);
        when(service.salvar(Mockito.any(Livro.class)))
                .thenThrow(new GeneroNaoEncontradoException(livroSemId.getGenero().getId()));

        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroInput)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeNaoEncontradaException.class))
                .andDo(print());

        verify(service, Mockito.times(1)).salvar(Mockito.any(Livro.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um livro com idEditora nao cadastrada Quando chamar POST Entao deve retornar status 404")
    void adicionarLivro_ComIdEditoraNaoCadastrada_RetornaNotFound() throws Exception {
        when(livroInputDisassembler.toDomainObject(Mockito.any(LivroInput.class))).thenReturn(livroSemId);
        when(service.salvar(Mockito.any(Livro.class)))
                .thenThrow(new EditoraNaoEncontradaException(livroSemId.getEditora().getId()));

        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroInput)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeNaoEncontradaException.class))
                .andDo(print());

        verify(service, Mockito.times(1)).salvar(Mockito.any(Livro.class));
        verifyNoMoreInteractions(service);
    }


    @Test
    @DisplayName("Dado um livro valido Quando chamar PUT Entao deve retornar status 200")
    void atualizarLivro_ComLivroValido_RetornarOK() throws Exception {
        when(service.buscarOuFalhar(anyLong())).thenReturn(livro);
        when(service.salvar(any(Livro.class))).thenReturn(livro);
        when(livroModelAssembler.toModel(any(Livro.class))).thenReturn(livroModel);

        mockMvc.perform(put("/livros/{livroId}", livroId)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(livroId));

        verify(service, times(1)).salvar(any(Livro.class));
    }


    @Test
    @DisplayName("Dado um livroId invalido Quando chamar PUT Entao deve retornar status 404")
    void atualizarLivro_ComLivroIdInvalido_RetornarNotFound() throws Exception {
        when(service.buscarOuFalhar(livroId)).thenThrow(new LivroNaoEncontradoException(livroId));

        mockMvc.perform(put("/livros/{livroId}", livroId)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroInput)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(LivroNaoEncontradoException.class));

        verify(service, never()).salvar(any(Livro.class));
    }

    @Test
    @DisplayName("Dado um livroId valido Quando chamar metodo excluir Entao deve retornar status 204")
    void removerLivro_ComLivroIdValido_RetornarNoContent() throws Exception {
        mockMvc.perform(delete("/livros/{livroId}",livroId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(service, times(1)).excluir(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um livroId invalido Quando chamar metodo excluir Entao deve retornar status 404")
    void removerLivro_ComLivroIdInvalido_RetornarNotFound() throws Exception {
        doThrow(new LivroNaoEncontradoException(livroId)).when(service).excluir(livroId);

        mockMvc.perform(delete("/livros/{livroId}",livroId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Dado um livroId valido em uso Quando chamar metodo excluir Entao deve retornar status 409")
    void removerLivro_ComLivroIdEmUso_RetornarConfict() throws Exception {
        doThrow(EntidadeEmUsoException.class).when(service).excluir(anyLong());

        mockMvc.perform(delete("/livros/{livroId}",livroId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(EntidadeEmUsoException.class))
                .andReturn();

        verify(service, times(1)).excluir(anyLong());
        verifyNoMoreInteractions(service);
    }

}