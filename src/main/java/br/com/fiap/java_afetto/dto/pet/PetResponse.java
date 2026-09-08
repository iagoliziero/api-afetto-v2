package br.com.fiap.java_afetto.dto.pet;

import br.com.fiap.java_afetto.model.pet.EspeciePet;
import br.com.fiap.java_afetto.model.pet.SexoPet;
import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.util.UUID;

public record PetResponse(

        UUID id,
        String nome,
        EspeciePet especie,
        String raca,
        SexoPet sexo,
        Float peso,
        LocalDate dataNasc,
        String descricao,

        Link linkPet,
        Link linkUsuario

) {
}