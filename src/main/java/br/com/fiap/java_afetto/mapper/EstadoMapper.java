package br.com.fiap.java_afetto.mapper;

import br.com.fiap.java_afetto.controller.endereco.EstadoController;
import br.com.fiap.java_afetto.controller.endereco.PaisController;
import br.com.fiap.java_afetto.dto.estado.EstadoLista;
import br.com.fiap.java_afetto.dto.estado.EstadoResponse;
import br.com.fiap.java_afetto.model.endereco.Estado;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EstadoMapper {

    public EstadoResponse estadoToResponse(Estado estado){
        Link linkEstado = linkTo(methodOn(EstadoController.class).readEstado(0)).withRel("Lista de estados");
        Link linkPais = linkTo(methodOn(PaisController.class).readPais(estado.getPais().getId())).withRel("Detalhes do país");
        return new EstadoResponse(estado.getId(), estado.getNome(), estado.getSigla(), linkEstado, linkPais);
    }

    public EstadoLista estadoToResponseLista(Estado estado){
        Link linkEstado = linkTo(methodOn(EstadoController.class).readEstado(estado.getId())).withRel("Detalhes do estado");
        Link linkPais = linkTo(methodOn(PaisController.class).readPais(estado.getPais().getId())).withRel("Detalhes do país");
        return new EstadoLista(estado.getNome(), estado.getSigla(), linkEstado, linkPais);
    }

}
