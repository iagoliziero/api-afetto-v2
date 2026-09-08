package br.com.fiap.java_afetto.dto.cidade;

import org.springframework.hateoas.Link;

import java.util.UUID;

public record CidadeResponse(UUID id, String nome, Link linkCidade, Link linkEstado) {
}
