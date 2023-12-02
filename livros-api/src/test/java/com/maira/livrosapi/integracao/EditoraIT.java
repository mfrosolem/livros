package com.maira.livrosapi.integracao;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static com.maira.livrosapi.common.EditoraConstants.AUTENTICA;
import static com.maira.livrosapi.common.EditoraConstants.EDITORA_INPUT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("it")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithUserDetails(value = "editora@livros.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class EditoraIT {

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
                .nome("CONSULTAR_EDITORA")
                .descricao("Pode pesquisar editora")
                .build();
        var permissaoCadastrar = Permissao.builder()
                .nome("CADASTRAR_EDITORA")
                .descricao("Pode cadastrar e editar editora")
                .build();

        var permissaoRemover = Permissao.builder()
                .nome("REMOVER_EDITORA")
                .descricao("Pode remover editora")
                .build();

        permissaoConsultar = this.permissaoService.salvar(permissaoConsultar);
        permissaoCadastrar = this.permissaoService.salvar(permissaoCadastrar);
        permissaoRemover = this.permissaoService.salvar(permissaoRemover);

        var grupo = Grupo.builder().nome("GRP_EDITORA")
                .permissoes(Set.of(permissaoConsultar, permissaoCadastrar, permissaoRemover)).build();
        grupo = this.grupoService.salvar(grupo);

        usuario = Usuario.builder()
                .nome("Usuario Teste")
                .email("editora@livros.com")
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
    @DisplayName("Quando chamar GET e nao existir editora Entao deve retornar status 200 e lista vazia")
    void listGeneros_ReturnsEmpty() throws Exception {
        mvc.perform(get("/editoras")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andDo(print());

    }

    @Test
    @DisplayName("Quando chamar GET Entao deve retornar status 200 e todos os editoras")
    @Sql(scripts = {"/scripts/import_editoras.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/remove_editoras.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void listGeneros_ReturnsAllGeneros() throws Exception {
        mvc.perform(get("/editoras")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET e filtrado por nome Entao deve retornar status 200 e lista")
    @Sql(scripts = {"/scripts/import_editoras.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/remove_editoras.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void listGeneros_ReturnsFiltered() throws Exception {
        mvc.perform(get("/editoras")
                        .param("nome", AUTENTICA.getNome())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].nome").value(AUTENTICA.getNome()))
                .andDo(print());
    }


    @Test
    @DisplayName("Quando chamar GET passando editoraId existente Entao deve retornar status 200 e o editora")
    @Sql(scripts = {"/scripts/import_editoras.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/remove_editoras.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void buscarGenero_RetornaGenero() throws Exception {
        mvc.perform(get("/editoras/{editoraId}", AUTENTICA.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(AUTENTICA.getNome()))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar GET passando editoraId inexistente Entao deve retornar status 404")
    void buscarGenero_RetornaNotFound() throws Exception {
        mvc.perform(get("/editoras/{editoraId}", AUTENTICA.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar POST passando editoraInput valido Entao deve retornar status 201")
    @Sql(scripts = {"/scripts/remove_editoras.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void adicionarGenero_RetornaCreate() throws Exception {

        mvc.perform(post("/editoras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(EDITORA_INPUT)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", equalTo(EDITORA_INPUT.getNome())))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar PUT passando editoraId e editoraInput validos Entao deve retornar status 200")
    @Sql(scripts = {"/scripts/import_editoras.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/remove_editoras.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void atualizarGenero_RetornaOk() throws Exception {

        mvc.perform(put("/editoras/{editoraId}", AUTENTICA.getId())
                        .content(objectMapper.writeValueAsString(EDITORA_INPUT))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(AUTENTICA.getId()))
                .andExpect(jsonPath("$.nome", equalTo(EDITORA_INPUT.getNome())))
                .andDo(print());
    }

    @Test
    @DisplayName("Quando chamar DELETE passando editoraId valido Entao deve retornar status 204")
    @Sql(scripts = {"/scripts/import_editoras.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/remove_editoras.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void removerGenero_RetornaNoContent() throws Exception {

        mvc.perform(delete("/editoras/{editoraId}", AUTENTICA.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


}
