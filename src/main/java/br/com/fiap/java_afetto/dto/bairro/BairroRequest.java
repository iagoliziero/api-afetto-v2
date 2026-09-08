package br.com.fiap.java_afetto.dto.bairro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record BairroRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 5, max = 45, message = "O nome deve ter entre 2 e 55 caracteres")
        String nome,
        @NotNull(message = "O id da cidade é obrigatório")
        UUID idCidade

) {
}
