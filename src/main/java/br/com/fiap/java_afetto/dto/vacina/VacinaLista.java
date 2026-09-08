package br.com.fiap.java_afetto.dto.vacina;

import org.springframework.hateoas.Link;

import java.time.LocalDate;

public record VacinaLista(

        String nomeVacina,
        LocalDate dataAplicacao,
        LocalDate proximaDose,

        Link linkVacina,
        Link linkPet

) {
}