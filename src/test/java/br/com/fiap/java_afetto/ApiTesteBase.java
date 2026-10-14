package br.com.fiap.java_afetto;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Base dos testes de API: sobe a aplicação inteira numa porta livre, com o
 * H2 em memória do perfil "test", e conversa com ela por HTTP, como o app faz.
 * Cada teste cria o próprio usuário (e-mail único), então um não atrapalha o outro.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
abstract class ApiTesteBase {

    @Autowired
    private Environment environment;

    private String urlBase;

    @BeforeEach
    void definirUrlBase() {
        String porta = environment.getProperty("local.server.port");
        assertNotNull(porta, "A aplicação de teste não informou a porta (local.server.port)");
        urlBase = "http://localhost:" + porta;
    }

    protected ClienteApi novoCliente() {
        return new ClienteApi(urlBase);
    }

    /** Cria um usuário novo, faz login e devolve o cliente já com a sessão. */
    protected UsuarioTeste novoUsuarioLogado() {

        ClienteApi cliente = novoCliente();
        String email = "teste-" + UUID.randomUUID() + "@afetto.com";

        ClienteApi.Resposta cadastro = cliente.post("/usuario", """
                {
                  "nome": "Tutor de Teste",
                  "cpf": "12345678909",
                  "dataNascimento": "1995-05-20",
                  "email": "%s",
                  "senha": "senha123",
                  "telefone": "11999999999"
                }
                """.formatted(email));
        assertEquals(201, cadastro.status(), cadastro.corpo());

        ClienteApi.Resposta login = cliente.post("/login", """
                { "email": "%s", "senha": "senha123" }
                """.formatted(email));
        assertEquals(200, login.status(), login.corpo());

        return new UsuarioTeste(cliente, cadastro.id(), email);
    }

    /** Cadastra um pet para o usuário e devolve o id dele. */
    protected String novoPet(UsuarioTeste usuario, String nome) {

        ClienteApi.Resposta pet = usuario.cliente().post("/pet", """
                {
                  "nome": "%s",
                  "especie": "CACHORRO",
                  "raca": "Vira-lata",
                  "sexo": "MACHO",
                  "peso": 12.5,
                  "dataNasc": "2022-03-10",
                  "idUsuario": "%s"
                }
                """.formatted(nome, usuario.id()));
        assertEquals(201, pet.status(), pet.corpo());

        return pet.id();
    }

    record UsuarioTeste(ClienteApi cliente, String id, String email) {
    }
}
