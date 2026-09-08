package br.com.fiap.java_afetto.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;
//String numero, String complemento, String cep, BigDecimal latitude, BigDecimal longitude
public record EnderecoRequest(

        @NotBlank(message = "O numero é obrigatório")
        String numero,
        String complemento,
        @NotBlank(message = "O cep é obrigatório")
        String cep,
        @NotNull(message = "A latitude é obrigatória")
        BigDecimal latitude,
        @NotNull(message = "A longitude é obrigatória")
        BigDecimal longitude,
        @NotNull(message = "O id do Logradouro é obrigatório")
        UUID idLogradouro

) {

}
