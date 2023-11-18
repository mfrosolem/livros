package com.maira.livrosapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.api.assembler.FotoLivroModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.api.model.FotoLivroModel;
import com.maira.livrosapi.api.model.input.FotoLivroInput;
import com.maira.livrosapi.domain.exception.FotoLivroNaoEncontradaException;
import com.maira.livrosapi.domain.model.*;
import com.maira.livrosapi.domain.service.FotoLivroService;
import com.maira.livrosapi.domain.service.FotoStorageService;
import com.maira.livrosapi.domain.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {LivroFotoController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class LivroFotoControllerTest {

    @MockBean
    FotoLivroService service;

    @MockBean
    FotoLivroModelAssembler fotoAssembler;

    @MockBean
    LivroService livroService;

    @MockBean
    FotoStorageService fotoStorage;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Livro livro;
    private FotoLivro fotoLivro;
    private Long livroId = 1L;
    private FotoLivroInput fotoLivroInput;
    private FotoLivroModel fotoLivroModel;

    private MockMultipartFile file;

    @BeforeEach
    public void init() {

        var genero = Genero.builder().id(1L).descricao("Romance").build();
        var editora = Editora.builder().id(1L).nome("Penguin & Companhia das Letras").build();
        var autor = Autor.builder().id(1L).nome("Joaquim Maria").sobrenome("Machado de Assis")
                .nomeConhecido("Machado de Assis").sexo("M").build();

        livro = Livro.builder().id(livroId).isbn("9788582850350").titulo("Dom Casmurro").idioma("Português")
                .paginas(390L).ano(2016L).genero(genero).autor(autor).editora(editora).build();

        fotoLivro = FotoLivro.builder().id(1L).livro(livro).contentType("image/jpeg")
                .nomeArquivo("759def91-6d60-4d84-ba57-3faad8930330_DOM_CASMURRO_1468012492180SK1468012492B.jpg")
                .descricao("Descrição da foto da capa").tamanho(500L).build();

        fotoLivroModel = FotoLivroModel.builder().contentType("image/jpeg")
                .nomeArquivo("759def91-6d60-4d84-ba57-3faad8930330_DOM_CASMURRO_1468012492180SK1468012492B.jpg")
                .descricao("Descrição da foto da capa").tamanho(500L).build();

        file = new MockMultipartFile(
                "file",
                "/home/maira/Área de Trabalho/Teste-Paynet/20231005/TransacaoVista20231005/PayloadTransacaoVista.png",
                MediaType.IMAGE_PNG_VALUE,
                "/home/maira/Área de Trabalho/Teste-Paynet/20231005/TransacaoVista20231005/PayloadTransacaoVista.png".getBytes()
        );

        fotoLivroInput = FotoLivroInput.builder().arquivo(file).descricao("Descrição da foto da capa").build();

    }

//    @Test
//    @DisplayName("Dado um livroId e fotLivroInput validos Quando chamar PUT Entao deve retornar status 200")
//    void atualizarFoto_ComLivroIdFotoLivroInputValidos_RetornaOk() throws Exception {
//    /* Nao consigo testar quando uso o content-type MULTIPART_FORM_DATA com o mockmvc.
//        Nao consigo passar o arquivo com chave valor entao o campo arquivo do FotoLivroInput fica null.
//        Consegui testar passando .file("arquivo", file.getBytes()), mas teria que desabilitar a annotation
//        @FileContentType pois o arquivo dessa forma nao tem o contentType e o nome do arquivo.*/
//
//        when(livroService.buscarOuFalhar(anyLong())).thenReturn(livro);
//        when(service.salvar(any(FotoLivro.class), any(InputStream.class))).thenReturn(fotoLivro);
//        when(fotoAssembler.toModel(any(FotoLivro.class))).thenReturn(fotoLivroModel);
//
//        mockMvc.perform(multipart(HttpMethod.PUT,"/livros/{livroId}/foto",livroId)
//                        .file(file)
//                        .param("descricao", fotoLivroInput.getDescricao())
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk());
//    }

    @Test
    @DisplayName("Dado um livroId valido Quando chamar GET Entao deve retornar a foto")
    void buscarFoto_ComLivroIdValido_RetornaFoto() throws Exception {
        when(service.buscarOuFalhar(anyLong())).thenReturn(fotoLivro);
        when(fotoAssembler.toModel(any(FotoLivro.class))).thenReturn(fotoLivroModel);

        mockMvc.perform(get("/livros/{livroId}/foto",livroId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(service, times(1)).buscarOuFalhar(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um livroId invalido Quando chamar GET Entao deve retornar status 404")
    void buscarFoto_ComLivroIdInvalido_RetornaNotFound() throws Exception {
        when(service.buscarOuFalhar(anyLong())).thenThrow(new FotoLivroNaoEncontradaException(livroId));

        mockMvc.perform(get("/livros/{livroId}/foto",livroId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FotoLivroNaoEncontradaException.class))
                .andDo(print());

        verify(service, times(1)).buscarOuFalhar(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um livroId valido Quando chamar DELETE Entao deve retornar status 204")
    void removerFoto_ComLivroIdValido_RetornaNoContent() throws Exception {
        mockMvc.perform(delete("/livros/{livroId}/foto",livroId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(service, times(1)).excluir(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Dado um livroId invalido Quando chamar DELETE Entao deve retornar status 404")
    void removerFoto_ComLivroIdInvalido_RetornaNotFound() throws Exception {
        doThrow(new FotoLivroNaoEncontradaException(livroId)).when(service).excluir(anyLong());

        mockMvc.perform(delete("/livros/{livroId}/foto",livroId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FotoLivroNaoEncontradaException.class))
                .andDo(print());

        verify(service, times(1)).excluir(anyLong());
        verifyNoMoreInteractions(service);
    }

}