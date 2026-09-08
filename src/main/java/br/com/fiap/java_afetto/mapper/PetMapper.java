package br.com.fiap.java_afetto.mapper;

import br.com.fiap.java_afetto.controller.PetController;
import br.com.fiap.java_afetto.controller.UsuarioController;
import br.com.fiap.java_afetto.dto.pet.PetLista;
import br.com.fiap.java_afetto.dto.pet.PetResponse;
import br.com.fiap.java_afetto.model.pet.Pet;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PetMapper {

    public PetResponse petToResponse(Pet pet) {

        Link linkPet =
                linkTo(
                        methodOn(PetController.class)
                                .readPets(0, 50)
                )
                        .withRel("Lista de pets");

        Link linkUsuario =
                linkTo(
                        methodOn(UsuarioController.class)
                                .readUsuario(pet.getUsuario().getId())
                )
                        .withRel("Detalhes do usuário");

        return new PetResponse(
                pet.getId(),
                pet.getNome(),
                pet.getEspecie(),
                pet.getRaca(),
                pet.getSexo(),
                pet.getPeso(),
                pet.getDataNasc(),
                pet.getDescricao(),
                linkPet,
                linkUsuario
        );
    }


    public PetLista petToResponseLista(Pet pet) {

        Link linkPet =
                linkTo(
                        methodOn(PetController.class)
                                .readPet(pet.getId())
                )
                        .withRel("Detalhes do pet");

        Link linkUsuario =
                linkTo(
                        methodOn(UsuarioController.class)
                                .readUsuario(pet.getUsuario().getId())
                )
                        .withRel("Detalhes do usuário");

        return new PetLista(
                pet.getId(),
                pet.getNome(),
                pet.getEspecie(),
                pet.getRaca(),
                linkPet,
                linkUsuario
        );
    }
}