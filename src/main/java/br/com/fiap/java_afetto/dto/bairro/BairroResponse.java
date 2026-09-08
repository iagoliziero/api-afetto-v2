package br.com.fiap.java_afetto.dto.bairro;

import org.springframework.hateoas.Link;

import java.util.UUID;

public record BairroResponse(UUID id, String nome, Link linkBairro, Link linkCidade) {
}
