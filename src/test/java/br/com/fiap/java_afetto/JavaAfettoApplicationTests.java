package br.com.fiap.java_afetto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Sobe a aplicação inteira (com o H2 em memória do perfil "test")
 * e confirma que todas as configurações carregam sem erro.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class JavaAfettoApplicationTests {

	@Test
	void contextLoads() {
	}

}
