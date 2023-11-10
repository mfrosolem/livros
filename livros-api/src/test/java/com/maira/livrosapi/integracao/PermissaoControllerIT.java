package com.maira.livrosapi.integracao;

import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.service.GrupoService;
import com.maira.livrosapi.domain.service.PermissaoService;
import com.maira.livrosapi.domain.service.UsuarioService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PermissaoControllerIT {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PermissaoService permissaoService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private UsuarioService usuarioService;

    Usuario usuarioConsulta;

    Usuario usuarioCadastro;

    Usuario usuarioSemPermissao;



    @BeforeAll
    public void init() {
        var permissao1 = Permissao.builder()
                .nome("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES")
                .descricao("Permite consultar usuários, grupos e permissões")
                .build();
        var permissao2 = Permissao.builder()
                .nome("CADASTRAR_USUARIOS_GRUPOS_PERMISSOES")
                .descricao("Permite criar ou editar usuários, grupos e permissões")
                .build();

        permissao1 = this.permissaoService.salvar(permissao1);
        permissao2 = this.permissaoService.salvar(permissao2);

        var grupoConsulta = Grupo.builder().nome("CONSULTA").permissoes(Set.of(permissao1)).build();
        var grupoCadastro = Grupo.builder().nome("CADASTRO").permissoes(Set.of(permissao1, permissao2)).build();

        grupoConsulta = this.grupoService.salvar(grupoConsulta);
        grupoCadastro = this.grupoService.salvar(grupoCadastro);

        usuarioConsulta = Usuario.builder()
                .nome("joao")
                .email("joao@livros.com")
                .senha("123")
                .grupos(Set.of(grupoConsulta))
                .build();

        usuarioCadastro = Usuario.builder()
                .nome("maria")
                .email("maria@livros.com")
                .senha("123")
                .grupos(Set.of(grupoCadastro))
                .build();

        usuarioSemPermissao = Usuario.builder()
                .nome("teste")
                .email("teste@livros.com")
                .senha("123")
                .build();

        usuarioConsulta = this.usuarioService.salvar(usuarioConsulta);
        usuarioCadastro = this.usuarioService.salvar(usuarioCadastro);
        usuarioSemPermissao = this.usuarioService.salvar(usuarioSemPermissao);
    }

    @BeforeEach
    public void setup() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Nao Deve permitir acessar permissoes sem autenticacao")
    void naoDevePermitirAcessarPermissoesSemAutenticacao() throws Exception {
        mvc.perform(get("/permissoes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    //@WithMockUser(value = "admin@livros.com", authorities = "ROLE")
    @WithUserDetails(value = "teste@livros.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Nao Deve permitir acessar permissoes quando o usuário autenticado mas sem permissao")
    void naoDevePermitirAcessarPermissoesSemAutorizacao() throws Exception {
        mvc.perform(get("/permissoes")
                        //.with(user("admin@livros.com").authorities(new SimpleGrantedAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "joao@livros.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Deve permitir listar permissoes quando usuario autenticado e com permissao")
    void devePermitirListarPermissoes() throws Exception {
        mvc.perform(get("/permissoes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



}