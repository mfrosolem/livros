package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.NegocioException;
import com.maira.livrosapi.domain.exception.UsuarioNaoEncontradoException;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private GrupoService grupoService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Usuario usuario;
    private Long usuarioId = 1L;

    private Grupo grupo;

    @BeforeEach
    public void init() {
        var permissao = Permissao.builder()
                .id(1L)
                .nome("CADASTRAR_USUARIOS_GRUPOS_PERMISSOES")
                .descricao("Permite criar ou editar usuários, grupos e permissões")
                .build();

        grupo = Grupo.builder()
                .id(1L)
                .nome("Visitante")
                .permissoes(Set.of(permissao))
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
    public void Dado_um_usuarioId_valido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_um_usuario_com_id() {
        this.buscarUsuarioPorIdComSucesso();

        Usuario usuarioRetornado = service.buscarOuFalhar(usuarioId);

        assertInstanceOf(Usuario.class, usuarioRetornado);
        assertEquals(usuarioId, usuarioRetornado.getId());
    }

    @Test
    @DisplayName("Dado um usuarioId invalido Quando chamar metodo buscarOuFalhar Entao deve lancar exception UsuarioNaoEncontradoException")
    void Dado_um_usuarioId_invalido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_lancar_exception_UsuarioNaoEncontradoException() {
        when(repository.findById(anyLong()))
                .thenThrow(new UsuarioNaoEncontradoException(usuarioId));

        UsuarioNaoEncontradoException exception =
                assertThrows(UsuarioNaoEncontradoException.class, () -> service.buscarOuFalhar(usuarioId));

        assertEquals(String.format("Não existe cadastro de usuário com o código %d", usuarioId), exception.getMessage());
        verify(repository, Mockito.times(1)).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }


    @Test
    @DisplayName("Dado um usuario valido que não existe no sistema Quando chamar salvar Entao deve retornar um usuario com id")
    void Dado_um_usuario_valido_novo_Quando_salvar_Entao_deve_retornar_um_usuario_com_id() {
        when(repository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(repository.save(Mockito.any(Usuario.class)))
                .thenAnswer(invocation -> {
                   Usuario usuarioParametro =  invocation.getArgument(0, Usuario.class);
                   usuarioParametro.setId(usuarioId);
                   return usuarioParametro;
                });

        this.mockPasswordEncoderSucesso();

        Usuario usuarioSalvo = service.salvar(usuario);

        assertInstanceOf(Usuario.class, usuarioSalvo);
        assertNotNull(usuarioSalvo.getId());
        assertEquals(usuarioId, usuarioSalvo.getId());
    }

    @Test
    @DisplayName("Dado um usuario valido e novo Quando chamar salvar Entao deve chamar o PasswordEncoder")
    void Dado_um_usuario_valido_novo_Quando_salvar_Entao_deve_chamar_PasswordEncoder() {
        when(repository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(repository.save(Mockito.any(Usuario.class)))
                .thenAnswer(invocation -> {
                    Usuario usuarioParametro =  invocation.getArgument(0, Usuario.class);
                    usuarioParametro.setId(usuarioId);
                    return usuarioParametro;
                });

        this.mockPasswordEncoderSucesso();

        Usuario usuarioSalvo = service.salvar(usuario);

        assertInstanceOf(Usuario.class, usuarioSalvo);
        assertNotNull(usuarioSalvo.getId());
        verify(passwordEncoder, Mockito.times(1)).encode(any());
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

        NegocioException negocioException =
                assertThrows(NegocioException.class, () -> service.salvar(usuario));

        assertEquals(String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()),
                negocioException.getMessage());
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

        when(repository.save(Mockito.any(Usuario.class)))
                .thenAnswer(invocation -> {
                    Usuario usuarioParametro =  invocation.getArgument(0, Usuario.class);
                    return usuarioParametro;
                });

        usuario.setId(usuarioId);

        Usuario usuarioEditado = service.salvar(usuario);

        assertEquals(usuario.getId(), usuarioEditado.getId());
        verifyNoInteractions(passwordEncoder);

    }

    @Test
    @DisplayName("Quando chamar alterarSenha se usuario nao encontrado Entao deve retornar exception UsuarioNaoEncontradoException")
    void alterarSenhaFalhaUsuarioNaoExiste() {
        when(repository.findById(anyLong()))
                .thenThrow(new UsuarioNaoEncontradoException(usuarioId));

        UsuarioNaoEncontradoException exception =
                assertThrows(UsuarioNaoEncontradoException.class,
                        () -> service.alterarSenha(usuarioId, "123", "321"));

        assertEquals(String.format("Não existe cadastro de usuário com o código %d", usuarioId), exception.getMessage());
    }

    @Test
    @DisplayName("Quando chamar alterarSenha se usuario informar senha atual incorreta Entao deve retornar exception NegocioException")
    void alterarSenhaFalhaSenhaAtualUsuario() {
        this.buscarUsuarioPorIdComSucesso();

        NegocioException exception =
                assertThrows(NegocioException.class,
                        () -> service.alterarSenha(usuarioId, "124", "321"));

        assertEquals("Senha atual informada não coincide com a senha do usuário.", exception.getMessage());
    }


    private void mockPasswordEncoderSucesso() {
        when(passwordEncoder.encode(any(CharSequence.class)))
                .thenReturn(UUID.randomUUID().toString());
    }

    private void buscarUsuarioPorIdComSucesso() {
        when(repository.findById(Mockito.anyLong())).thenAnswer(invocation -> {
            Long idLivroPassado = invocation.getArgument(0, Long.class);
            usuario.setId(idLivroPassado);
            usuario.setGrupos(Set.of(grupo));
            return Optional.of(usuario);
        });
    }


}