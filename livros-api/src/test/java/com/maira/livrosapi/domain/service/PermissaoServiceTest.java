package com.maira.livrosapi.domain.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


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

    @Test
    @DisplayName("Quando chamar metodo listByNameContaining Entao deve retornar todas as permissoes")
    void listByNameContaining_RetornaTodasPermissoes() {
        List<Permissao> permissoes = new ArrayList<>() {
            {
                add(Permissao.builder().id(1L).nome("VISITANTE").descricao("Visitante").build());
                add(Permissao.builder().id(2L).nome("ADMIN").descricao("Administrador").build());
                add(Permissao.builder().id(3L).nome("DIGITADOR").descricao("Digitador").build());
            }
        };
        Pageable pageable = PageRequest.of(0,20);
        Page<Permissao> page = new PageImpl<>(permissoes,pageable, permissoes.size());
        when(repository.findByNomeContaining(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Permissao> sut = service.listByNameContaining("", pageable);

        assertThat(sut).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("Quando chamar metodo listByNameContaining filtrando por nome inexistente Entao deve retornar lista vazia")
    void listByNameContaining_Filtrando_RetornaListaVazia() {
        List<Permissao> permissoes = new ArrayList<>();
        Pageable pageable = PageRequest.of(0,20);
        Page<Permissao> page = new PageImpl<>(permissoes,pageable, permissoes.size());
        when(repository.findByNomeContaining(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Permissao> sut = service.listByNameContaining("Visitante", pageable);
        assertThat(sut).isEmpty();
    }



}