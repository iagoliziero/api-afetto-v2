package br.com.fiap.java_afetto.dto.cidade;

import org.springframework.hateoas.Link;

public record CidadeLista(String nome, Link linkCidade, Link linkEstado){


}
