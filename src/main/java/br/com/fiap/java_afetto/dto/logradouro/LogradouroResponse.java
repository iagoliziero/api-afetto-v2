package br.com.fiap.java_afetto.dto.logradouro;

import org.springframework.hateoas.Link;

import java.util.UUID;

public record LogradouroResponse(UUID id, String nome, Link linkLogradouro, Link linkBairro) {
}
