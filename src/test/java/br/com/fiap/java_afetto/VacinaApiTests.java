package br.com.fiap.java_afetto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testes do CRUD de Vacina (TBL_VACINA), ligada ao Pet. */
class VacinaApiTests extends ApiTesteBase {

    private String novaVacina(UsuarioTeste tutor, String idPet, String nome, LocalDate proximaDose) {

        ClienteApi.Resposta vacina = tutor.cliente().post("/vacina", """
                {
                  "nomeVacina": "%s",
                  "fabricante": "Zoetis",
                  "lote": "L2026A1",
                  "dataAplicacao": "2026-01-15",
                  "proximaDose": "%s",
                  "observacoes": "Sem reações",
                  "idPet": "%s"
                }
                """.formatted(nome, proximaDose, idPet));
        assertEquals(201, vacina.status(), vacina.corpo());

        return vacina.id();
    }

    @Test
    @DisplayName("Registra, consulta, lista, atualiza e exclui uma vacina")
    void crudCompletoDaVacina() {

        UsuarioTeste tutor = novoUsuarioLogado();
        ClienteApi api = tutor.cliente();
        String idPet = novoPet(tutor, "Bolt");

        // Inserir
        String idVacina = novaVacina(tutor, idPet, "Antirrabica", LocalDate.now().plusYears(1));

        // Consultar por id
        ClienteApi.Resposta consulta = api.get("/vacina/" + idVacina);
        assertEquals(200, consulta.status(), consulta.corpo());
        assertTrue(consulta.corpo().contains("Antirrabica"), consulta.corpo());

        // Listar
        ClienteApi.Resposta lista = api.get("/vacina");
        assertEquals(200, lista.status(), lista.corpo());
        assertTrue(lista.corpo().contains("Antirrabica"), lista.corpo());

        // Atualizar
        ClienteApi.Resposta atualizada = api.put("/vacina/" + idVacina, """
                {
                  "nomeVacina": "Antirrabica",
                  "fabricante": "Zoetis",
                  "lote": "L2026B9",
                  "dataAplicacao": "2026-01-15",
                  "proximaDose": "%s",
                  "observacoes": "Reforço confirmado",
                  "idPet": "%s"
                }
                """.formatted(LocalDate.now().plusYears(1), idPet));
        assertEquals(200, atualizada.status(), atualizada.corpo());
        assertTrue(atualizada.corpo().contains("L2026B9"), atualizada.corpo());

        // Excluir
        ClienteApi.Resposta exclusao = api.delete("/vacina/" + idVacina);
        assertEquals(204, exclusao.status(), exclusao.corpo());

        ClienteApi.Resposta depois = api.get("/vacina/" + idVacina);
        assertNotEquals(200, depois.status(), "a vacina excluída ainda foi encontrada");
    }

    @Test
    @DisplayName("Pet com vacina vencida fica com status ATENCAO")
    void statusVacinacaoComVacinaVencida() {

        UsuarioTeste tutor = novoUsuarioLogado();
        String idPet = novoPet(tutor, "Mia");

        novaVacina(tutor, idPet, "V10", LocalDate.now().minusDays(10));

        ClienteApi.Resposta status = tutor.cliente().get("/pet/" + idPet + "/status-vacinacao");
        assertEquals(200, status.status(), status.corpo());
        assertTrue(status.corpo().contains("ATENCAO"), status.corpo());
    }

    @Test
    @DisplayName("Pet com vacinas longe de vencer fica com status EM_DIA")
    void statusVacinacaoEmDia() {

        UsuarioTeste tutor = novoUsuarioLogado();
        String idPet = novoPet(tutor, "Thor");

        novaVacina(tutor, idPet, "V10", LocalDate.now().plusMonths(6));

        ClienteApi.Resposta status = tutor.cliente().get("/pet/" + idPet + "/status-vacinacao");
        assertEquals(200, status.status(), status.corpo());
        assertTrue(status.corpo().contains("EM_DIA"), status.corpo());
    }

    @Test
    @DisplayName("Não aceita vacina sem pet")
    void vacinaSemPetRetorna400() {

        UsuarioTeste tutor = novoUsuarioLogado();

        ClienteApi.Resposta resposta = tutor.cliente().post("/vacina", """
                {
                  "nomeVacina": "V10",
                  "dataAplicacao": "2026-01-15"
                }
                """);

        assertEquals(400, resposta.status(), resposta.corpo());
        assertTrue(resposta.corpo().contains("idPet"), resposta.corpo());
    }
}
