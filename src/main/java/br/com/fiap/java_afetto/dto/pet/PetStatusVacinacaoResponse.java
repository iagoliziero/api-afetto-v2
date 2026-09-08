package br.com.fiap.java_afetto.dto.pet;

public record PetStatusVacinacaoResponse(
        String pet,
        int totalVacinas,
        int vacinasAtrasadas,
        int proximasVacinas,
        int vacinasEmDia,
        String status
) {
}