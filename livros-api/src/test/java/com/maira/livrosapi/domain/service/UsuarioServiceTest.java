package com.maira.livrosapi.domain.service;

import static org.junit.jupiter.api.Assertions.*;

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

import java.util.Optional;
import java.util.Set;

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
                .build();

    }

    @Test
    @DisplayName("Dado um usuarioId valido Quando chamar metodo buscarOuFalhar Entao deve retornar um usuario com id")
    public void Dado_um_usuarioId_valido_Quando_chamar_metodo_buscarOuFalhar_Entao_deve_retornar_um_usuario_com_id() {
        when(repository.findById(Mockito.anyLong())).thenAnswer(invocation -> {
            Long idLivroPassado = invocation.getArgument(0, Long.class);
            usuario.setId(idLivroPassado);
            usuario.setGrupos(Set.of(grupo));
            return Optional.of(usuario);
        });

        Usuario usuarioRetornado = service.buscarOuFalhar(usuarioId);

        assertInstanceOf(Usuario.class, usuarioRetornado);
        assertEquals(usuarioId, usuarioRetornado.getId());
    }


}