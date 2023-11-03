package com.maira.livrosapi.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.maira.livrosapi.domain.exception.PermissaoNaoEncontradaException;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.repository.PermissaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class PermissaoServiceTest {

    @InjectMocks
    private  PermissaoService service;

    @Mock
    private PermissaoRepository repository;

    Permissao permissao;

    Long permissaoId;

    @BeforeEach
    void init() {
        permissao = Permissao.builder()
                .nome("ROLE_PERMISSAO_PESQUISAR")
                .descricao("Pesquisar permissões")
                .build();

        permissaoId = 1L;
    }

    @Test
    @DisplayName("Dado uma permissaoId valida Quando chamar metodo buscarOuFalhar Entao deve retornar uma permissao com id")
    void Dado_uma_permissaoId_valida_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_uma_permissao_com_id() {

        when(repository.findById(anyLong())).thenAnswer(answer -> {
            Long permissaoIdPassada = answer.getArgument(0, Long.class);
            permissao.setId(permissaoIdPassada);
            return Optional.of(permissao);
        });

        Permissao sut = service.buscarOuFalhar(permissaoId);

        assertThat(sut).isInstanceOf(Permissao.class);
        assertThat(sut.getId()).isNotNull();
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Dada uma permissaoId invalida Quando chamar metodo buscarOuFalhar Entao deve lancar exception PermissaoNaoEncontradaException")
    void Dada_uma_permissaoId_invalida_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_PermissaoNaoEncontradaException() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarOuFalhar(permissaoId))
                .isInstanceOf(PermissaoNaoEncontradaException.class);
    }

    @Test
    @DisplayName("Dado uma permissao valida Quando salvar Entao deve retornar uma permissao com id")
    void Dado_uma_permissao_valida_Quando_salvar_Entao_deve_retornar_uma_permissao_com_id() {
        when(repository.save(any(Permissao.class)))
                .thenAnswer(invocation -> {
                    Permissao permissaoPassada = invocation.getArgument(0, Permissao.class);
                    permissaoPassada.setId(permissaoId);
                    return permissaoPassada;
                });

        Permissao sut = service.salvar(permissao);

       assertThat(sut).isInstanceOf(Permissao.class);
       assertThat(sut.getId()).isNotNull();

    }


}