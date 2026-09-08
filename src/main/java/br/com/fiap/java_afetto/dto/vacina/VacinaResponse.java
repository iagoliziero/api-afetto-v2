package br.com.fiap.java_afetto.dto.vacina;

import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.util.UUID;

public record VacinaResponse(

        UUID id,
        String nomeVacina,
        String fabricante,
        String lote,
        LocalDate dataAplicacao,
        LocalDate proximaDose,
        String observacoes,

        Link linkVacinas,
        Link linkPet

) {
}