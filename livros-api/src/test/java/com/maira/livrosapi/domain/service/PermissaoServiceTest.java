package com.maira.livrosapi.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import com.maira.livrosapi.domain.exception.PermissaoNaoEncontradaException;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.repository.PermissaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
    void Dado_uma_permissaoId_valida_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_uma_permissao_com_id() {

        Mockito.when(repository.findById(Mockito.anyLong())).thenAnswer(answer -> {
            Long permissaoIdPassada = answer.getArgument(0, Long.class);
            permissao.setId(permissaoIdPassada);
            return Optional.of(permissao);
        });

        Permissao permissaoRetornada = service.buscarOuFalhar(permissaoId);

        assertNotNull(permissaoRetornada.getId());
        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void Dado_uma_permissaoId_invalida_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_PermissaoNaoEncontradaException() {
        Mockito.when(repository.findById(permissaoId))
                .thenThrow(new PermissaoNaoEncontradaException(permissaoId));

        assertThrows(PermissaoNaoEncontradaException.class, () -> service.buscarOuFalhar(permissaoId));
    }

    @Test
    void Dado_uma_permissao_valida_Quando_salvar_Entao_deve_retornar_uma_permissao_com_id() {
        Mockito.when(repository.save(Mockito.any(Permissao.class)))
                .thenAnswer(invocation -> {
                    Permissao permissaoPassada = invocation.getArgument(0, Permissao.class);
                    permissaoPassada.setId(permissaoId);
                    return permissaoPassada;
                });

        Permissao permissaoSalva = service.salvar(permissao);

        assertNotNull(permissaoSalva.getId());
        assertEquals(permissao.getNome(), permissaoSalva.getNome());
        assertEquals(permissao.getDescricao(), permissaoSalva.getDescricao());
    }


}