package br.com.fiap.java_afetto.dto.pais;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaisRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 45, message = "O nome deve ter entre 2 e 45 caracteres")
        String nome,
        @NotBlank(message = "A sigla é obrigatória")
        @Size(min = 2, max = 5, message = "O nome deve ter entre 2 e 5 caracteres")
        String sigla

) {
}
