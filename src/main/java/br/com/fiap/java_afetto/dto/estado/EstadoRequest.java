package br.com.fiap.java_afetto.dto.estado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EstadoRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 45, message = "O nome deve ter entre 2 e 45 caracteres")
        String nome,
        @NotBlank(message = "A sigla é obrigatória")
        @Size(min = 2, max = 5, message = "O nome deve ter entre 2 e 5 caracteres")
        String sigla,
        @NotNull(message = "O id do País é obrigatório")
        UUID idPais

) {
}
