package br.com.fiap.java_afetto.dto.logradouro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record LogradouroRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 5, max = 200, message = "O nome deve ter entre 5 e 200 caracteres")
        String nome,
        @NotNull(message = "O id do bairro é obrigatório")
        UUID idBairro

) {
}
