package br.com.fiap.java_afetto.dto.pais;

import org.springframework.hateoas.Link;

import java.util.UUID;

public record PaisLista(UUID idPais, String nome, String sigla, Link link){



}
