package br.com.fiap.java_afetto.dto.bairro;

import org.springframework.hateoas.Link;

public record BairroLista(String nome, Link linkBairro, Link linkCidade){


}
