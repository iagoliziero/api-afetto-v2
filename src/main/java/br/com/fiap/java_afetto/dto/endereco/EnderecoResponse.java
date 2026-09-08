package br.com.fiap.java_afetto.dto.endereco;

import org.springframework.hateoas.Link;

import java.math.BigDecimal;
import java.util.UUID;

public record EnderecoResponse(UUID id, String numero, String complemento, String cep, BigDecimal latitude, BigDecimal longitude, Link linkEndereco, Link linkLogradouro) {
}
