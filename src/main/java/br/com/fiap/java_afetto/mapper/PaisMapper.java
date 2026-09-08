package br.com.fiap.java_afetto.mapper;

import br.com.fiap.java_afetto.controller.endereco.PaisController;
import br.com.fiap.java_afetto.dto.pais.PaisLista;
import br.com.fiap.java_afetto.dto.pais.PaisResponse;
import br.com.fiap.java_afetto.model.endereco.Pais;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PaisMapper {

    public PaisResponse paisToResponse(Pais pais){
        Link link = linkTo(methodOn(PaisController.class).readPais(0)).withRel("Lista de países");
        return new PaisResponse(pais.getId(), pais.getNome(), pais.getSigla(), link);

    }

    public PaisLista paisToResponseLista(Pais pais){
        Link link = linkTo(methodOn(PaisController.class).readPais(pais.getId())).withRel("Detalhes do país");
        return new PaisLista(pais.getId(), pais.getNome(), pais.getSigla(), link);
    }

}
