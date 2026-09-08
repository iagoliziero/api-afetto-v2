package br.com.fiap.java_afetto.dto.estado;

import org.springframework.hateoas.Link;

import java.util.UUID;

public record EstadoLista(String nome, String sigla, Link linkEstado, Link linkPais){


}
