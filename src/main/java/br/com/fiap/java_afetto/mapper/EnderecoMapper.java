package br.com.fiap.java_afetto.mapper;

import br.com.fiap.java_afetto.controller.endereco.BairroController;
import br.com.fiap.java_afetto.controller.endereco.EnderecoController;
import br.com.fiap.java_afetto.controller.endereco.LogradouroController;
import br.com.fiap.java_afetto.dto.endereco.EnderecoLista;
import br.com.fiap.java_afetto.dto.endereco.EnderecoResponse;
import br.com.fiap.java_afetto.dto.logradouro.LogradouroLista;
import br.com.fiap.java_afetto.dto.logradouro.LogradouroResponse;
import br.com.fiap.java_afetto.model.endereco.Endereco;
import br.com.fiap.java_afetto.model.endereco.Logradouro;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EnderecoMapper {

    public EnderecoResponse enderecoToResponse(Endereco endereco){
        Link linkEndereco = linkTo(methodOn(EnderecoController.class).readEndereco(0)).withRel("Lista de endereços");
        Link linkLogradouro = linkTo(methodOn(LogradouroController.class).readLogradouro(endereco.getLogradouro().getId())).withRel("Detalhes do logradouro");
        return new EnderecoResponse(endereco.getId(), endereco.getNumero(), endereco.getComplemento(), endereco.getCep(), endereco.getLatitude(), endereco.getLongitude(), linkEndereco, linkLogradouro);
    }

    public EnderecoLista enderecoToResponseLista(Endereco endereco) {
        Link linkEndereco = linkTo(methodOn(EnderecoController.class).readEndereco(endereco.getId())).withRel("Detalhes do endereço");
        Link linkLogradouro = linkTo(methodOn(LogradouroController.class).readLogradouro(endereco.getLogradouro().getId())).withRel("Detalhes do logradouro");
        return new EnderecoLista(endereco.getNumero(), endereco.getComplemento(), endereco.getCep(), endereco.getLatitude(), endereco.getLongitude(), linkEndereco, linkLogradouro);
    }

}
