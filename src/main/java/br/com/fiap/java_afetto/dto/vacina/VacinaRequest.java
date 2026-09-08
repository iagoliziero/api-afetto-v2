package br.com.fiap.java_afetto.dto.vacina;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record VacinaRequest(

        @NotBlank(message = "O nome da vacina é obrigatório")
        String nomeVacina,

        String fabricante,

        String lote,

        @NotNull(message = "A data de aplicação é obrigatória")
        LocalDate dataAplicacao,

        LocalDate proximaDose,

        String observacoes,

        @NotNull(message = "O pet é obrigatório")
        UUID idPet,

        UUID idHistorico

) {
}