package com.maira.livrosapi.integracao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maira.livrosapi.domain.model.Grupo;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.model.Usuario;
import com.maira.livrosapi.domain.service.GrupoService;
import com.maira.livrosapi.domain.service.PermissaoService;
import com.maira.livrosapi.domain.service.UsuarioService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static com.maira.livrosapi.common.GeneroConstants.GENERO_INPUT;
import static com.maira.livrosapi.common.GeneroConstants.ROMANCE;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("it")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithUserDetails(value = "genero@livros.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class GeneroIT {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PermissaoService permissaoService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    Usuario usuario;


    @BeforeAll
    public void initAll() {
        var permissaoConsultar = Permissao.builder()
                .nome("CONSULTAR_GENERO")
                .descricao("Pode pesquisar genero")
                .build();
        var permissaoCadastrar = Permissao.builder()
                .nome("CADASTRAR_GENERO")
                .descricao("Pode cadastrar e editar genero")
                .build();

        var permissaoRemover = Permissao.builder()
                .nome("REMOVER_GENERO")
                .descricao("Pode remover genero")
                .build();

        permissaoConsultar = this.permissaoService.salvar(permissaoConsultar);
        permissaoCadastrar = this.permissaoService.salvar(permissaoCadastrar);
        permissaoRemover = this.permissaoService.salvar(permissaoRemover);

        var grupo = Grupo.builder().nome("GRP_GENERO")
                .permissoes(Set.of(permissaoConsultar, permissaoCadastrar, permissaoRemover)).build();
        grupo = this.grupoService.salvar(grupo);

        usuario = Usuario.builder()
                .nome("Usuario Teste")
                .email("genero@livros.com")
                .senha("123")
                .grupo(grupo)
                .build();
        usuario = this.usuarioService.salvar(usuario);
    }


    @BeforeEach
    public void setupEach() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    @DisplayName("Quando chamar GET e nao existir genero Entao deve retornar status 200 e lista vazia")
    void listGeneros_ReturnsEmpty() throws Exception {
        mvc.perform(get("/generos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andDo(print());

    }

    @Test
    @DisplayName("Quando chamar GET Entao deve retornar status 200 e todos os generos")
    @Sql(scripts = {"/scripts/import_generos.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/remove_generos.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void listGeneros_ReturnsAllGeneros() throws Exception {
        mvc.perform(get("/generos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET e filtrado por descricao Entao deve retornar status 200 e lista")
    @Sql(scripts = {"/scripts/import_generos.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/remove_generos.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void listGeneros_ReturnsFiltered() throws Exception {
        mvc.perform(get("/generos")
                        .param("descricao", ROMANCE.getDescricao())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].descricao").value(ROMANCE.getDescricao()))
                .andDo(print());
    }


    @Test
    @DisplayName("Quando chamar GET passando generoId existente Entao deve retornar status 200 e o genero")
    @Sql(scripts = {"/scripts/import_generos.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/remove_generos.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void buscarGenero_RetornaGenero() throws Exception {
        mvc.perform(get("/generos/{generoId}", ROMANCE.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value(ROMANCE.getDescricao()))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando generoId inexistente Entao deve retornar status 404")
    void buscarGenero_RetornaNotFound() throws Exception {
        mvc.perform(get("/generos/{generoId}", ROMANCE.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar POST passando generoInput valido Entao deve retornar status 201")
    @Sql(scripts = {"/scripts/remove_generos.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void adicionarGenero_RetornaCreate() throws Exception {

        mvc.perform(post("/generos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(GENERO_INPUT)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao", Matchers.equalTo(GENERO_INPUT.getDescricao())))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar PUT passando generoId e generoInput validos Entao deve retornar status 200")
    @Sql(scripts = {"/scripts/import_generos.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/remove_generos.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void atualizarGenero_RetornaOk() throws Exception {

        mvc.perform(put("/generos/{generoId}", ROMANCE.getId())
                        .content(objectMapper.writeValueAsString(GENERO_INPUT))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ROMANCE.getId()))
                .andExpect(jsonPath("$.descricao", Matchers.equalTo(GENERO_INPUT.getDescricao())))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar DELETE passando generoId valido Entao deve retornar status 204")
    @Sql(scripts = {"/scripts/import_generos.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/remove_generos.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void removerGenero_RetornaNoContent() throws Exception {

        mvc.perform(delete("/generos/{generoId}", ROMANCE.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


}
