package br.com.fiap.java_afetto.dto.cidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CidadeRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 45, message = "O nome deve ter entre 2 e 45 caracteres")
        String nome,
        @NotNull(message = "O id do Estado é obrigatório")
        UUID idEstado

) {
}
