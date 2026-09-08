package br.com.fiap.java_afetto.mapper;

import br.com.fiap.java_afetto.controller.UsuarioController;
import br.com.fiap.java_afetto.controller.endereco.EnderecoController;
import br.com.fiap.java_afetto.dto.usuario.UsuarioLista;
import br.com.fiap.java_afetto.dto.usuario.UsuarioResponse;
import br.com.fiap.java_afetto.model.Usuario;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioMapper {

    public UsuarioResponse usuarioToResponse(Usuario usuario){
        Link linkEndereco = usuario.getEndereco() != null ?
                linkTo(methodOn(EnderecoController.class).readEndereco(usuario.getEndereco().getId())).withRel("Detalhes do endereço") :null;
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getCpf(), usuario.getEmail(), usuario.getDataNascimento(), usuario.getTelefone(), linkEndereco);
    }

    public UsuarioLista usuarioToResponseLista(Usuario usuario){
        Link linkUsuario = linkTo(methodOn(UsuarioController.class).readUsuario(usuario.getId())).withRel("Detalhes do usuário");
        Link linkEndereco = usuario.getEndereco() != null ?
                linkTo(methodOn(EnderecoController.class).readEndereco(usuario.getEndereco().getId())).withRel("Detalhes do endereço"): null;
        return new UsuarioLista(usuario.getNome(), usuario.getTelefone(), usuario.getRole(), linkUsuario, linkEndereco);
    }

}
