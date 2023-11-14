package com.maira.livrosapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.api.assembler.FotoLivroModelAssembler;
import com.maira.livrosapi.api.exception.ApiExceptionHandler;
import com.maira.livrosapi.domain.service.FotoLivroService;
import com.maira.livrosapi.domain.service.FotoStorageService;
import com.maira.livrosapi.domain.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

}