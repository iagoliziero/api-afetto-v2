package br.com.fiap.java_afetto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cliente HTTP usado nos testes. Guarda o cookie da sessão (JSESSIONID) que o
 * POST /login devolve e manda nas próximas chamadas, igual ao app.
 */
class ClienteApi {

    private static final Pattern ID =
            Pattern.compile("\"id\"\\s*:\\s*\"([0-9a-fA-F-]{36})\"");

    private final HttpClient http = HttpClient.newHttpClient();
    private final String urlBase;
    private String cookieSessao;

    ClienteApi(String urlBase) {
        this.urlBase = urlBase;
    }

    Resposta get(String caminho) {
        return enviar("GET", caminho, null);
    }

    Resposta post(String caminho, String json) {
        return enviar("POST", caminho, json);
    }

    Resposta put(String caminho, String json) {
        return enviar("PUT", caminho, json);
    }

    Resposta delete(String caminho) {
        return enviar("DELETE", caminho, null);
    }

    private Resposta enviar(String metodo, String caminho, String json) {

        HttpRequest.Builder pedido = HttpRequest
                .newBuilder(URI.create(urlBase + caminho))
                .header("Accept", "application/json");

        if (json != null) {
            pedido.header("Content-Type", "application/json")
                    .method(metodo, HttpRequest.BodyPublishers.ofString(json));
        } else {
            pedido.method(metodo, HttpRequest.BodyPublishers.noBody());
        }

        if (cookieSessao != null) {
            pedido.header("Cookie", cookieSessao);
        }

        try {
            HttpResponse<String> resposta =
                    http.send(pedido.build(), HttpResponse.BodyHandlers.ofString());

            resposta.headers().allValues("Set-Cookie").stream()
                    .filter(cookie -> cookie.startsWith("JSESSIONID="))
                    .findFirst()
                    .ifPresent(cookie -> cookieSessao = cookie.split(";", 2)[0]);

            return new Resposta(resposta.statusCode(), resposta.body());

        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(
                    "Falha ao chamar " + metodo + " " + caminho, e
            );
        }
    }

    record Resposta(int status, String corpo) {

        // Primeiro "id" do JSON: nas respostas da API é o id do próprio recurso
        String id() {
            Matcher matcher = ID.matcher(corpo);
            if (!matcher.find()) {
                throw new IllegalStateException("Resposta sem id: " + corpo);
            }
            return matcher.group(1);
        }
    }
}
