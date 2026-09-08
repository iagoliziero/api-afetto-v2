package br.com.fiap.java_afetto.mapper;

import br.com.fiap.java_afetto.controller.endereco.BairroController;
import br.com.fiap.java_afetto.controller.endereco.CidadeController;
import br.com.fiap.java_afetto.controller.endereco.LogradouroController;
import br.com.fiap.java_afetto.dto.bairro.BairroLista;
import br.com.fiap.java_afetto.dto.bairro.BairroResponse;
import br.com.fiap.java_afetto.dto.logradouro.LogradouroLista;
import br.com.fiap.java_afetto.dto.logradouro.LogradouroResponse;
import br.com.fiap.java_afetto.model.endereco.Bairro;
import br.com.fiap.java_afetto.model.endereco.Logradouro;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LogradouroMapper {

    public LogradouroResponse logradouroToResponse(Logradouro logradouro){
        Link linkLogradouro = linkTo(methodOn(LogradouroController.class).readLogradouro(0)).withRel("Lista de logradouros");
        Link linkBairro = linkTo(methodOn(BairroController.class).readBairro(logradouro.getBairro().getId())).withRel("Detalhes do bairro");
        return new LogradouroResponse(logradouro.getId(), logradouro.getNome(), linkLogradouro, linkBairro);
    }

    public LogradouroLista logradouroToResponseLista(Logradouro logradouro) {
        Link linkLogradouro = linkTo(methodOn(LogradouroController.class).readLogradouro(logradouro.getId())).withRel("Detalhes do logradouro");
        Link linkBairro = linkTo(methodOn(BairroController.class).readBairro(logradouro.getBairro().getId())).withRel("Detalhes do bairro");
        return new LogradouroLista(logradouro.getNome(), linkLogradouro, linkBairro);
    }

}
