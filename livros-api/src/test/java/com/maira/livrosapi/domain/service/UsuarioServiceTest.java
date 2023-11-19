package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.GrupoNaoEncontradoException;
import com.maira.livrosapi.domain.exception.NegocioException;
import com.maira.livrosapi.domain.exception.UsuarioNaoEncontradoException;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.repository.UsuarioRepository;
import com.maira.livrosapi.domain.service.impl.UsuarioServiceImpl;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioServiceImpl service;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private GrupoService grupoService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Usuario usuario;
    private Long usuarioId = 1L;

    private Grupo grupo;
    private Long grupoId = 1L;

    private Permissao permissao;
    private Long permissaoId = 1L;

    @BeforeEach
    public void init() {
        var permissao = Permissao.builder()
                .id(permissaoId)
                .nome("CADASTRAR_USUARIOS_GRUPOS_PERMISSOES")
                .descricao("Permite criar ou editar usuários, grupos e permissões")
                .build();

        Set<Permissao> permissoes = new HashSet<>(){{ add(permissao); }};

        grupo = Grupo.builder()
                .id(grupoId)
                .nome("Visitante")
                .permissoes(permissoes)
                .build();

        usuario = Usuario.builder()
                .nome("joao")
                .email("joao@livros")
                .senha("123")
                .dataCadastro(OffsetDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Dado um usuarioId valido Quando chamar metodo buscarOuFalhar Entao deve retornar um usuario com id")
    void Dado_um_usuarioId_valido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_um_usuario_com_id() {
        this.buscarUsuarioPorIdComSucesso();

        Usuario sut = service.buscarOuFalhar(usuarioId);

        assertThat(sut.getId()).isNotNull();
        assertThat(sut).isInstanceOf(Usuario.class);
        assertThat(sut.getId()).isEqualTo(usuarioId);
    }

    @Test
    @DisplayName("Dado um usuarioId invalido Quando chamar metodo buscarOuFalhar Entao deve lancar exception UsuarioNaoEncontradoException")
    void Dado_um_usuarioId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_UsuarioNaoEncontradoException() {
        this.buscarUsuarioPorIdComFalha();

        assertThatThrownBy(() -> service.buscarOuFalhar(usuarioId))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(String.format("Não existe cadastro de usuário com o código %d", usuarioId));

        verify(repository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }


    @Test
    @DisplayName("Dado um usuario valido que não existe no sistema Quando chamar salvar Entao deve retornar um usuario com id")
    void Dado_um_usuario_valido_novo_Quando_salvar_Entao_deve_retornar_um_usuario_com_id() {
        when(repository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(repository.save(any(Usuario.class)))
                .thenAnswer(invocation -> {
                   Usuario usuarioParametro =  invocation.getArgument(0, Usuario.class);
                   usuarioParametro.setId(usuarioId);
                   return usuarioParametro;
                });

        this.mockPasswordEncoderSucesso();

        Usuario sut = service.salvar(usuario);

        assertThat(sut).isInstanceOf(Usuario.class);
        assertThat(sut.getId()).isNotNull();
        assertThat(sut.getId()).isEqualTo(usuarioId);

    }

    @Test
    @DisplayName("Dado um usuario valido e novo Quando chamar salvar Entao deve chamar o PasswordEncoder")
    void Dado_um_usuario_valido_novo_Quando_salvar_Entao_deve_chamar_PasswordEncoder() {
        when(repository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(repository.save(any(Usuario.class)))
                .thenAnswer(invocation -> {
                    Usuario usuarioParametro =  invocation.getArgument(0, Usuario.class);
                    usuarioParametro.setId(usuarioId);
                    return usuarioParametro;
                });

        this.mockPasswordEncoderSucesso();

        Usuario sut = service.salvar(usuario);

        assertThat(sut).isInstanceOf(Usuario.class);
        assertThat(sut.getId()).isNotNull();
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    @DisplayName("Dado um usuario valido e novo que o e-mail ja existe Quando salvar Entao deve retornar exception NegocioException")
    void Dado_um_usuario_valido_que_ja_existe_Quando_salvar_Entao_deve_retornar_exception_NegocioException() {
        when(repository.findByEmail(anyString()))
                .thenAnswer(invocation -> {
                    String emailPassado = invocation.getArgument(0);
                    var usuarioEncontrado = Usuario.builder()
                            .id(2L)
                            .nome("joao")
                            .email(emailPassado)
                            .senha("123")
                            .build();
                    return Optional.of(usuarioEncontrado);
                });

        assertThatThrownBy(() -> service.salvar(usuario))
                .isInstanceOf(NegocioException.class)
                .hasMessage(String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
    }

    @Test
    @DisplayName("Dado um usuario valido na edicao Quando chamar medtodo salvar Entao nao deve chamar PasswordEncoder")
    void Dado_um_usuario_valido_na_edicao_Quando_chamar_salvar_Entao_nao_deve_chamar_PasswordEncoder() {
        when(repository.findByEmail(anyString()))
                .thenAnswer(invocation -> {
                    String emailPassado = invocation.getArgument(0);
                    var usuarioEncontrado = Usuario.builder()
                            .id(usuarioId)
                            .nome("joao")
                            .email(emailPassado)
                            .senha("123")
                            .build();
                    return Optional.of(usuarioEncontrado);
                });

        when(repository.save(any(Usuario.class)))
                .thenAnswer(invocation -> {
                    Usuario usuarioParametro =  invocation.getArgument(0, Usuario.class);
                    return usuarioParametro;
                });

        usuario.setId(usuarioId);

        Usuario sut = service.salvar(usuario);

        assertThat(sut.getId()).isEqualTo(usuario.getId());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("Quando chamar alterarSenha Entao deve chamar encode")
    void alterarSenhaSucesso() {
        this.buscarUsuarioPorIdComSucesso();
        this.mockPasswordEncoderSucesso();
        when(passwordEncoder.matches(any(CharSequence.class), anyString())).thenReturn(Boolean.TRUE);

        assertThatCode(() -> service.alterarSenha(usuarioId, "123", "123456")).doesNotThrowAnyException();

        verify(passwordEncoder, times(1)).encode(any(CharSequence.class));
    }

    @Test
    @DisplayName("Quando chamar alterarSenha se usuario nao encontrado Entao deve retornar exception UsuarioNaoEncontradoException")
    void alterarSenhaFalhaUsuarioNaoExiste() {
        when(repository.findById(anyLong()))
                .thenThrow(new UsuarioNaoEncontradoException(usuarioId));

        assertThatThrownBy(() -> service.alterarSenha(usuarioId, "123", "321"))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(String.format("Não existe cadastro de usuário com o código %d", usuarioId));
    }

    @Test
    @DisplayName("Quando chamar alterarSenha se usuario informar senha atual incorreta Entao deve retornar exception NegocioException")
    void alterarSenhaFalhaSenhaAtualUsuario() {
        this.buscarUsuarioPorIdComSucesso();
        when(passwordEncoder.matches(any(CharSequence.class), anyString())).thenReturn(Boolean.FALSE);

        assertThatThrownBy(() -> service.alterarSenha(usuarioId, "124", "321"))
                .isInstanceOf(NegocioException.class)
                .hasMessage("Senha atual informada não coincide com a senha do usuário.");
    }

    @Test
    @DisplayName("Dado um usuarioId valido Quando chamar metodo excluir Entao deve excluir usuario")
    void Dado_um_usuarioId_valido_Quando_chamar_metodo_excluir_Entao_deve_excluir_usuario() {
        assertThatCode(() -> service.excluir(usuarioId)).doesNotThrowAnyException();

        verify(repository, times(1)).deleteById(anyLong());
        verify(repository, times(1)).flush();
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Dado um usuarioId invalido Quando chamar metodo excluir Entao deve lancar exception UsuarioNaoEncontradoException")
    void Dado_um_usuarioId_invalido_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_UsuarioNaoEncontradoException() {
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());

        assertThatThrownBy(() -> service.excluir(usuarioId))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(String.format("Não existe cadastro de usuário com o código %d", usuarioId));
    }


    @Test
    @DisplayName("Dado um usuarioId em uso Quando chamar metodo excluir Entao deve lancar exception EntidadeEmUsoException")
    void Dado_um_usuarioId_em_uso_Quando_chamar_metodo_excluir_Entao_deve_lancar_exception_EntidadeEmUsoException() {
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());

        assertThatThrownBy(() -> service.excluir(usuarioId)).isInstanceOf(EntidadeEmUsoException.class);
    }

    @Test
    @DisplayName("Dado um usuario valido e um grupo valido Quando chamar metodo associarGrupo Entao deve fazer a associação")
    void Dado_um_usuario_valido_e_grupo_valido_Quando_chamar_metodo_associarGrupo_Entao_deve_fazer_associacao() {
        var grupo2 = Grupo.builder().id(2L).nome("ADMIN").build();
        usuario.setId(usuarioId);
        Set<Grupo> grupos = new HashSet<>(){ { add(grupo); } };
        usuario.setGrupos(grupos);
        when(repository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(grupoService.buscarOuFalhar(anyLong())).thenReturn(grupo2);

        assertThatCode(() -> service.associarGrupo(usuario.getId(), grupo2.getId())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Dado um usuarioId invalido e grupoId valido Quando chamar metodo associarGrupo Entao deve lancar exception UsuarioNaoEncontradoException")
    void Dado_um_usuarioId_invalido_e_grupoId_valido_Quando_chamar_metodo_associarGrupo_Entao_deve_lancar_exception_UsuarioNaoEncontradoException() {
        this.buscarUsuarioPorIdComFalha();

        assertThatThrownBy(() -> service.associarGrupo(usuarioId, grupoId))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(String.format("Não existe cadastro de usuário com o código %d", usuarioId));
    }

    @Test
    @DisplayName("Dado um usuarioId valido e um grupoId invalido Quando chamar metodo associarGrupo Entao deve lancar exception GrupoNaoEncontradoException")
    void Dado_um_usuarioId_valido_e_um_grupoId_invalido_Quando_chamar_metodo_associarGrupo_Entao_deve_lancar_exception_GrupoNaoEncontradoException() {
        this.buscarUsuarioPorIdComSucesso();
        this.buscarGrupoPorIdComFalha();

        assertThatThrownBy(() -> service.associarGrupo(usuarioId, grupoId))
                .isInstanceOf(GrupoNaoEncontradoException.class)
                .hasMessage(String.format("Não existe cadastro de grupo com o código %d", grupoId));
    }

    @Test
    @DisplayName("Dado um usuario valido e uma grupo valido Quando chamar metodo desassociarGrupo Entao deve fazer a desassociação")
    void Dado_um_usuario_valido_e_grupo_valido_Quando_chamar_metodo_desassociarGrupo_Entao_deve_fazer_desassociacao() {

        usuario.setId(usuarioId);
        Set<Grupo> grupos = new HashSet<>(){ { add(grupo); } };
        usuario.setGrupos(grupos);
        when(repository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(grupoService.buscarOuFalhar(anyLong())).thenReturn(grupo);

        assertThatCode(() -> service.desassociarGrupo(usuario.getId(), grupo.getId())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Dado um usuarioId invalido e um grupoId valido Quando chamar metodo desassociarGrupo Entao deve lancar exception UsuarioNaoEncontradoException")
    void Dado_um_usuarioId_invalido_e_um_grupoId_valido_Quando_chamar_metodo_desassociarGrupo_Entao_deve_lancar_exception_UsuarioNaoEncontradoException() {
        this.buscarUsuarioPorIdComFalha();

        assertThatThrownBy(() -> service.desassociarGrupo(usuarioId, grupoId))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(String.format("Não existe cadastro de usuário com o código %d", usuarioId));
    }

    @Test
    @DisplayName("Dado um usuarioId valido e um grupoId invalido Quando chamar metodo desassociarGrupo Entao deve lancar exception GrupoNaoEncontradoException")
    void Dado_um_usuarioId_valido_e_um_grupoId_invalido_Quando_chamar_metodo_desassociarGrupo_Entao_deve_lancar_exception_GrupoNaoEncontradoException() {
        this.buscarUsuarioPorIdComSucesso();
        this.buscarGrupoPorIdComFalha();

        assertThatThrownBy(() -> service.desassociarGrupo(usuarioId, grupoId))
                .isInstanceOf(GrupoNaoEncontradoException.class)
                .hasMessage(String.format("Não existe cadastro de grupo com o código %d", grupoId));
    }

    @Test
    @DisplayName("Quando chamar metodo listByNomeContaining Entao deve retornar todos os usuarios")
    void listByNomeContaining_RetornaTodosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>() {
            {
                add(Usuario.builder().id(1L).nome("joao").email("joao@livros").senha("123").dataCadastro(OffsetDateTime.now()).build());
                add(Usuario.builder().id(2L).nome("jose").email("jose@livros").senha("123").dataCadastro(OffsetDateTime.now()).build());
                add(Usuario.builder().id(3L).nome("maria").email("maria@livros").senha("123").dataCadastro(OffsetDateTime.now()).build());
            }
        };
        Pageable pageable = PageRequest.of(0,20);
        Page<Usuario> page = new PageImpl<>(usuarios,pageable, usuarios.size());
        when(repository.findByNomeContaining(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Usuario> sut = service.listByContaining("", pageable);

        assertThat(sut).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("Quando chamar metodo listByNomeContaining filtrando por descricao inexistente Entao deve retornar lista vazia")
    void listByNomeContaining_Filtrando_RetornaListaVazia() {
        List<Usuario> usuarios = new ArrayList<>();
        Pageable pageable = PageRequest.of(0,20);
        Page<Usuario> page = new PageImpl<>(usuarios,pageable, usuarios.size());
        when(repository.findByNomeContaining(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Usuario> sut = service.listByContaining("Romance", pageable);
        assertThat(sut).isEmpty();
    }


    private void mockPasswordEncoderSucesso() {
        when(passwordEncoder.encode(any(CharSequence.class)))
                .thenReturn(UUID.randomUUID().toString());
    }

    private void buscarUsuarioPorIdComSucesso() {
        when(repository.findById(anyLong())).thenAnswer(invocation -> {
            Long idUsuarioPassado = invocation.getArgument(0, Long.class);
            usuario.setId(idUsuarioPassado);
            usuario.setGrupos(Set.of(grupo));
            return Optional.of(usuario);
        });
    }

    private void buscarUsuarioPorIdComFalha() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());
    }

    private void buscarGrupoPorIdComFalha() {
        when(grupoService.buscarOuFalhar(anyLong()))
                .thenThrow(new GrupoNaoEncontradoException(grupoId));
    }

}
