package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.NegocioException;
import com.maira.livrosapi.domain.exception.PermissaoNaoEncontradaException;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.repository.PermissaoRepository;
import com.maira.livrosapi.domain.service.impl.PermissaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PermissaoServiceTest {

    @InjectMocks
    private PermissaoServiceImpl service;

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
    @DisplayName("Dado uma permissao nova com nome existente Quando salvar Entao deve retornar exception NegocioException")
    void Dado_uma_permissao_nova_com_nome_existente_Quando_salvar_Entao_deve_retornar_exception_NegocioException() {
        when(repository.save(any(Permissao.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> service.salvar(permissao))
                .isInstanceOf(NegocioException.class)
                .hasMessage(String.format("Permissão de nome %s já cadastrada", permissao.getNome()));

        verify(repository, times(1)).save(any(Permissao.class));
    }

    @Test
    @DisplayName("Dado uma permissaoId valido Quando chamar metodo excluir Entao deve excluir permissao")
    void Dado_um_permissaoId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_permissao() {
        assertThatCode(() -> service.excluir(permissaoId)).doesNotThrowAnyException();

        verify(repository, times(1)).deleteById(anyLong());
        verify(repository, times(1)).flush();
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Dado uma permissaoId invalido Quando chamar metodo excluir Entao deve lancar exception PermissaoNaoEncontradaException")
    void Dado_um_permissaoId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_PermissaoNaoEncontradaException() {
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());

        assertThatThrownBy(() -> service.excluir(permissaoId))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                .hasMessage(String.format("Não existe cadastro de permissão com o código %d", permissaoId));
    }


    @Test
    @DisplayName("Dado uma permissaoId em uso Quando chamar metodo excluir Entao deve lancar exception EntidadeEmUsoException")
    void Dado_um_permissaoId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());

        assertThatThrownBy(() -> service.excluir(permissaoId)).isInstanceOf(EntidadeEmUsoException.class)
                .hasMessage(String.format("Permissão de código %d não pode ser removida, pois está em uso", permissaoId));
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

        Page<Permissao> sut = service.listByContaining("", pageable);

        assertThat(sut).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("Quando chamar metodo listByNameContaining filtrando por nome inexistente Entao deve retornar lista vazia")
    void listByNameContaining_Filtrando_RetornaListaVazia() {
        List<Permissao> permissoes = new ArrayList<>();
        Pageable pageable = PageRequest.of(0,20);
        Page<Permissao> page = new PageImpl<>(permissoes,pageable, permissoes.size());
        when(repository.findByNomeContaining(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Permissao> sut = service.listByContaining("Visitante", pageable);
        assertThat(sut).isEmpty();
    }



}