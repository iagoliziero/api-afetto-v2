package br.com.fiap.java_afetto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testes do CRUD de Pet (TBL_PET), passando pela API como o app faz. */
class PetApiTests extends ApiTesteBase {

    @Test
    @DisplayName("Cadastra, consulta, lista, atualiza e exclui um pet")
    void crudCompletoDoPet() {

        UsuarioTeste tutor = novoUsuarioLogado();
        ClienteApi api = tutor.cliente();

        // Inserir
        String idPet = novoPet(tutor, "Rex");

        // Consultar por id
        ClienteApi.Resposta consulta = api.get("/pet/" + idPet);
        assertEquals(200, consulta.status(), consulta.corpo());
        assertTrue(consulta.corpo().contains("Rex"), consulta.corpo());

        // Listar
        ClienteApi.Resposta lista = api.get("/pet");
        assertEquals(200, lista.status(), lista.corpo());
        assertTrue(lista.corpo().contains(idPet), lista.corpo());

        // Atualizar
        ClienteApi.Resposta atualizado = api.put("/pet/" + idPet, """
                {
                  "nome": "Rex Atualizado",
                  "especie": "CACHORRO",
                  "raca": "Labrador",
                  "sexo": "MACHO",
                  "peso": 27.0,
                  "dataNasc": "2022-03-10",
                  "descricao": "Ganhou peso",
                  "idUsuario": "%s"
                }
                """.formatted(tutor.id()));
        assertEquals(200, atualizado.status(), atualizado.corpo());
        assertTrue(atualizado.corpo().contains("Rex Atualizado"), atualizado.corpo());
        assertTrue(atualizado.corpo().contains("Labrador"), atualizado.corpo());

        // Excluir
        ClienteApi.Resposta exclusao = api.delete("/pet/" + idPet);
        assertEquals(204, exclusao.status(), exclusao.corpo());

        ClienteApi.Resposta depois = api.get("/pet/" + idPet);
        assertNotEquals(200, depois.status(), "o pet excluído ainda foi encontrado");
    }

    @Test
    @DisplayName("Não aceita pet sem nome")
    void petSemNomeRetorna400() {

        UsuarioTeste tutor = novoUsuarioLogado();

        ClienteApi.Resposta resposta = tutor.cliente().post("/pet", """
                {
                  "nome": "",
                  "especie": "GATO",
                  "sexo": "FEMEA",
                  "idUsuario": "%s"
                }
                """.formatted(tutor.id()));

        assertEquals(400, resposta.status(), resposta.corpo());
        assertTrue(resposta.corpo().contains("nome"), resposta.corpo());
    }

    @Test
    @DisplayName("Sem login, a lista de pets é bloqueada")
    void semLoginNaoAcessaPets() {

        ClienteApi.Resposta resposta = novoCliente().get("/pet");

        assertTrue(resposta.status() == 401 || resposta.status() == 403,
                "esperava 401 ou 403, veio " + resposta.status());
    }
}
