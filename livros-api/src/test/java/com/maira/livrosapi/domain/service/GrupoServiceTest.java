package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GrupoNaoEncontradoException;
import com.maira.livrosapi.domain.exception.NegocioException;
import com.maira.livrosapi.domain.exception.PermissaoNaoEncontradaException;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.repository.GrupoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrupoServiceTest {

    @InjectMocks
    private GrupoService service;

    @Mock
    private GrupoRepository repository;

    @Mock
    private PermissaoService permissaoService;

    private Grupo grupo;
    private Grupo grupoSemId;
    private Long grupoId = 1L;
    private Permissao permissao;
    private Long permissaoId = 1L;

    @BeforeEach
    void init(){

        permissao = Permissao.builder().id(1L).nome("VISITANTE").descricao("Visitante").build();

        Set<Permissao> permissoes = new HashSet<>() {{add(permissao);}};

        grupo = Grupo.builder().id(grupoId).nome("Usuario Comum").permissoes(permissoes).build();
        grupoSemId = Grupo.builder().nome("Usuario Comum").permissoes(permissoes).build();
    }

    @Test
    @DisplayName("Dado um grupoId valido Quando chamar metodo buscarOuFalhar Entao deve retornar um grupo com id")
    void Dado_um_grupoId_valido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_um_grupo_com_id() {
        this.buscarGrupoPorIdComSucesso();

        Grupo sut = service.buscarOuFalhar(grupoId);

        assertThat(sut.getId()).isNotNull();
        assertThat(sut).isInstanceOf(Grupo.class);
        assertThat(sut.getId()).isEqualTo(grupoId);
    }

    @Test
    @DisplayName("Dado um grupoId invalido Quando chamar metodo buscarOuFalhar Entao deve lancar exception GrupoNaoEncontradoException")
    void Dado_um_grupoId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_GrupoNaoEncontradoException() {
        this.buscarGrupoPorIdComFalha();

        assertThatThrownBy(() -> service.buscarOuFalhar(grupoId))
                .isInstanceOf(GrupoNaoEncontradoException.class)
                .hasMessage(String.format("Não existe cadastro de grupo com o código %d", grupoId));

        verify(repository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }


    @Test
    @DisplayName("Dado um grupo valido que não existe no sistema Quando chamar salvar Entao deve retornar um grupo com id")
    void Dado_um_grupo_valido_novo_Quando_salvar_Entao_deve_retornar_um_grupo_com_id() {
        when(repository.save(any(Grupo.class)))
                .thenAnswer(invocation -> {
                    Grupo grupoParametro =  invocation.getArgument(0, Grupo.class);
                    grupoParametro.setId(grupoId);
                    return grupoParametro;
                });

        Grupo sut = service.salvar(grupo);

        assertThat(sut).isInstanceOf(Grupo.class);
        assertThat(sut.getId()).isNotNull();
        assertThat(sut.getId()).isEqualTo(grupoId);
    }

    @Test
    @DisplayName("Dado um grupo novo com nome existente Quando salvar Entao deve retornar exception NegocioException")
    void Dado_um_grupo_novo_com_nome_existente_Quando_salvar_Entao_deve_retornar_exception_NegocioException() {
        when(repository.save(any(Grupo.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> service.salvar(grupo))
                .isInstanceOf(NegocioException.class)
                .hasMessage(String.format("Grupo de nome %s já cadastrado", grupo.getNome()));

        verify(repository, times(1)).save(any(Grupo.class));
    }


    @Test
    @DisplayName("Dado um grupoId valido Quando chamar metodo excluir Entao deve excluir grupo")
    void Dado_um_grupoId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_grupo() {
        assertThatCode(() -> service.excluir(grupoId)).doesNotThrowAnyException();

        verify(repository, times(1)).deleteById(anyLong());
        verify(repository, times(1)).flush();
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Dado um grupoId invalido Quando chamar metodo excluir Entao deve lancar exception GrupoNaoEncontradoException")
    void Dado_um_grupoId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_GrupoNaoEncontradoException() {
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());

        assertThatThrownBy(() -> service.excluir(grupoId))
                .isInstanceOf(GrupoNaoEncontradoException.class)
                .hasMessage(String.format("Não existe cadastro de grupo com o código %d", grupoId));
    }


    @Test
    @DisplayName("Dado um grupoId em uso Quando chamar metodo excluir Entao deve lancar exception EntidadeEmUsoException")
    void Dado_um_grupoId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());

        assertThatThrownBy(() -> service.excluir(grupoId)).isInstanceOf(EntidadeEmUsoException.class)
                .hasMessage(String.format("Grupo de código %d não pode ser removido, está em uso", grupoId));
    }

    @Test
    @DisplayName("Dado um grupo valido e uma permissao valida Quando chamar metodo associarGrupo Entao deve fazer a associação")
    void Dado_um_grupo_valido_e_uma_permissao_valida_Quando_chamar_metodo_associarGrupo_Entao_deve_fazer_associacao() {
        var permissao2 = Permissao.builder().id(2L).nome("CONSULTAR").descricao("Consultar").build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(grupo));
        when(permissaoService.buscarOuFalhar(anyLong())).thenReturn(permissao2);

        assertThatCode(() -> service.associarPermissao(grupo.getId(), permissao2.getId())).doesNotThrowAnyException();
    }


    @Test
    @DisplayName("Dado um grupoId invalido e uma permissaoId valida Quando chamar metodo associarGrupo Entao deve lancar exception GrupoNaoEncontradoException")
    void Dado_um_grupoId_invalido_e_uma_permissaoId_valida_Quando_chamar_metodo_associarGrupo_Entao_deve_lancar_exception_GrupoNaoEncontradoException() {
        this.buscarGrupoPorIdComFalha();

        assertThatThrownBy(() -> service.associarPermissao(grupoId, permissaoId))
                .isInstanceOf(GrupoNaoEncontradoException.class)
                .hasMessage(String.format("Não existe cadastro de grupo com o código %d", grupoId));
    }


    @Test
    @DisplayName("Dado um grupoId valido e uma permissaoId invalida Quando chamar metodo associarGrupo Entao deve lancar exception PermissaoNaoEncontradaException")
    void Dado_um_grupoId_valido_e_uma_permissaoId_invalida_Quando_chamar_metodo_associarGrupo_Entao_deve_lancar_exception_PermissaoNaoEncontradaException() {
        this.buscarGrupoPorIdComSucesso();
        this.buscarPermissaoPorIdComFalha();

        assertThatThrownBy(() -> service.associarPermissao(grupoId, permissaoId))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                .hasMessage(String.format("Não existe cadastro de permissão com o código %d", grupoId));
    }

    @Test
    @DisplayName("Dado um grupo valido e uma permissao valida Quando chamar metodo desassociarGrupo Entao deve fazer a desassociacao")
    void Dado_um_grupo_valido_e_permissao_valida_Quando_chamar_metodo_desassociarGrupo_Entao_deve_fazer_desassociacao() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(grupo));
        when(permissaoService.buscarOuFalhar(anyLong())).thenReturn(permissao);

        assertThatCode(() -> service.desassociarPermissao(grupo.getId(), permissao.getId())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Dado um grupoId invalido e uma permissaoId valida Quando chamar metodo desassociarGrupo Entao deve lancar exception GrupoNaoEncontradoException")
    void Dado_um_grupoId_invalido_e_uma_permissaoId_valida_Quando_chamar_metodo_desassociarGrupo_Entao_deve_lancar_exception_GrupoNaoEncontradoException() {
        this.buscarGrupoPorIdComFalha();

        assertThatThrownBy(() -> service.desassociarPermissao(grupoId, permissaoId))
                .isInstanceOf(GrupoNaoEncontradoException.class)
                .hasMessage(String.format("Não existe cadastro de grupo com o código %d", grupoId));
    }

    @Test
    @DisplayName("Dado um grupoId valido e uma permissaoId invalida Quando chamar metodo desassociarGrupo Entao deve lancar exception PermissaoNaoEncontradaException")
    void Dado_um_grupoId_valido_e_uma_permissaoId_invalida_Quando_chamar_metodo_desassociarGrupo_Entao_deve_lancar_exception_PermissaoNaoEncontradaException() {
        this.buscarGrupoPorIdComSucesso();
        this.buscarPermissaoPorIdComFalha();

        assertThatThrownBy(() -> service.desassociarPermissao(grupoId, permissaoId))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                .hasMessage(String.format("Não existe cadastro de permissão com o código %d", grupoId));
    }

    @Test
    @DisplayName("Quando chamar metodo listByNomeContaining Entao deve retornar todos os grupos")
    void listByNomeContaining_RetornaTodosGrupos() {
        List<Grupo> grupos = new ArrayList<>() {
            {
                add(Grupo.builder().id(1L).nome("Visitante").build());
                add(Grupo.builder().id(2L).nome("Admin").build());
                add(Grupo.builder().id(3L).nome("Digitador").build());
            }
        };
        Pageable pageable = PageRequest.of(0,20);
        Page<Grupo> page = new PageImpl<>(grupos,pageable, grupos.size());
        when(repository.findByNomeContaining(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Grupo> sut = service.listyByNameContaining("", pageable);

        assertThat(sut).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("Quando chamar metodo listByNomeContaining filtrando por nome inexistente Entao deve retornar lista vazia")
    void listByNomeContaining_Filtrando_RetornaListaVazia() {
        List<Grupo> grupos = new ArrayList<>();
        Pageable pageable = PageRequest.of(0,20);
        Page<Grupo> page = new PageImpl<>(grupos,pageable, grupos.size());
        when(repository.findByNomeContaining(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Grupo> sut = service.listyByNameContaining("visita", pageable);
        assertThat(sut).isEmpty();
    }


    private void buscarGrupoPorIdComSucesso() {
        when(repository.findById(anyLong())).thenAnswer(invocation -> {
            Long idGrupoPassado = invocation.getArgument(0, Long.class);
            grupo.setId(idGrupoPassado);
            return Optional.of(grupo);
        });
    }

    private void buscarGrupoPorIdComFalha() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());
    }

    private void buscarPermissaoPorIdComFalha() {
        when(permissaoService.buscarOuFalhar(anyLong()))
                .thenThrow(new PermissaoNaoEncontradaException(permissaoId));
    }


}