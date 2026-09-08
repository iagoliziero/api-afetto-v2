package br.com.fiap.java_afetto.mapper;

import br.com.fiap.java_afetto.controller.endereco.CidadeController;
import br.com.fiap.java_afetto.controller.endereco.EstadoController;
import br.com.fiap.java_afetto.dto.cidade.CidadeLista;
import br.com.fiap.java_afetto.dto.cidade.CidadeResponse;
import br.com.fiap.java_afetto.model.endereco.Cidade;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CidadeMapper {

    public CidadeResponse cidadeToResponse(Cidade cidade){
        Link linkCidade = linkTo(methodOn(CidadeController.class).readCidade(0)).withRel("Lista de cidades");
        Link linkEstado = linkTo(methodOn(EstadoController.class).readEstado(cidade.getEstado().getId())).withRel("Detalhes do estado");
        return new CidadeResponse(cidade.getId(), cidade.getNome(), linkCidade, linkEstado);
    }

    public CidadeLista cidadeToResponseLista(Cidade cidade) {
        Link linkCidade = linkTo(methodOn(CidadeController.class).readCidade(cidade.getId())).withRel("Detalhes da cidade");
        Link linkEstado = linkTo(methodOn(EstadoController.class).readEstado(cidade.getEstado().getId())).withRel("Detalhes do estado");
        return new CidadeLista(cidade.getNome(), linkCidade, linkEstado);
    }
}
