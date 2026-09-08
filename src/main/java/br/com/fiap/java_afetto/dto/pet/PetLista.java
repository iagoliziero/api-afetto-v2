package br.com.fiap.java_afetto.dto.pet;

import br.com.fiap.java_afetto.model.pet.EspeciePet;
import org.springframework.hateoas.Link;

import java.util.UUID;

public record PetLista(

        UUID id,
        String nome,
        EspeciePet especie,
        String raca,

        Link linkPet,
        Link linkUsuario

) {
}