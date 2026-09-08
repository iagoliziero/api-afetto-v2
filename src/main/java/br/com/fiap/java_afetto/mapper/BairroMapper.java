package br.com.fiap.java_afetto.mapper;


import br.com.fiap.java_afetto.controller.endereco.BairroController;
import br.com.fiap.java_afetto.controller.endereco.CidadeController;
import br.com.fiap.java_afetto.dto.bairro.BairroLista;
import br.com.fiap.java_afetto.dto.bairro.BairroResponse;
import br.com.fiap.java_afetto.model.endereco.Bairro;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BairroMapper {

    public BairroResponse bairroToResponse(Bairro bairro){
        Link linkBairro = linkTo(methodOn(BairroController.class).readBairro(0)).withRel("Lista de bairros");
        Link linkCidade = linkTo(methodOn(CidadeController.class).readCidade(bairro.getCidade().getId())).withRel("Detalhes da cidade");
        return new BairroResponse(bairro.getId(), bairro.getNome(), linkBairro, linkCidade);
    }

    public BairroLista bairroToResponseLista(Bairro bairro) {
        Link linkBairro = linkTo(methodOn(BairroController.class).readBairro(bairro.getId())).withRel("Detalhes do bairro");
        Link linkCidade = linkTo(methodOn(CidadeController.class).readCidade(bairro.getCidade().getId())).withRel("Detalhes da cidade");
        return new BairroLista(bairro.getNome(), linkBairro, linkCidade);
    }
}
