package br.com.fiap.java_afetto.dto.logradouro;

import org.springframework.hateoas.Link;

public record LogradouroLista(String nome, Link linkLogradouro, Link linkBairro){


}
