package br.com.fiap.java_afetto.dto.pais;

import org.springframework.hateoas.Link;

import java.util.UUID;

public record PaisResponse(UUID id, String nome, String sigla, Link link) {
}
