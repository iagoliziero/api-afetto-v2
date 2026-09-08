package br.com.fiap.java_afetto.dto.endereco;

import org.springframework.hateoas.Link;

import java.math.BigDecimal;

public record EnderecoLista(String numero, String complemento, String cep, BigDecimal latitude, BigDecimal longitude, Link linkEndereco, Link linkLogradouro) {
}
