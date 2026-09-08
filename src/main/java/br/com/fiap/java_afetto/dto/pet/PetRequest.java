package br.com.fiap.java_afetto.dto.pet;

import br.com.fiap.java_afetto.model.pet.EspeciePet;
import br.com.fiap.java_afetto.model.pet.SexoPet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record PetRequest(

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotNull(message = "A espécie é obrigatória")
        EspeciePet especie,

        String raca,

        @NotNull(message = "O sexo é obrigatório")
        SexoPet sexo,

        @Positive(message = "O peso deve ser maior que zero")
        Float peso,

        LocalDate dataNasc,

        String descricao,

        @NotNull(message = "O usuário é obrigatório")
        UUID idUsuario

) {
}