package br.com.fiap.java_afetto.mapper;

import br.com.fiap.java_afetto.controller.PetController;
import br.com.fiap.java_afetto.controller.VacinaController;
import br.com.fiap.java_afetto.dto.vacina.VacinaLista;
import br.com.fiap.java_afetto.dto.vacina.VacinaResponse;
import br.com.fiap.java_afetto.model.Vacina;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VacinaMapper {

    public VacinaResponse vacinaToResponse(Vacina vacina) {

        Link linkVacinas =
                linkTo(
                        methodOn(VacinaController.class)
                                .readVacinas(0, 50)
                )
                        .withRel("Lista de vacinas");

        Link linkPet =
                linkTo(
                        methodOn(PetController.class)
                                .readPet(vacina.getPet().getId())
                )
                        .withRel("Detalhes do pet");

        return new VacinaResponse(
                vacina.getId(),
                vacina.getNomeVacina(),
                vacina.getFabricante(),
                vacina.getLote(),
                vacina.getDataAplicacao(),
                vacina.getProximaDose(),
                vacina.getObservacoes(),
                linkVacinas,
                linkPet
        );
    }

    public VacinaLista vacinaToResponseLista(Vacina vacina) {

        Link linkVacina =
                linkTo(
                        methodOn(VacinaController.class)
                                .readVacina(vacina.getId())
                )
                        .withRel("Detalhes da vacina");

        Link linkPet =
                linkTo(
                        methodOn(PetController.class)
                                .readPet(vacina.getPet().getId())
                )
                        .withRel("Detalhes do pet");

        return new VacinaLista(
                vacina.getNomeVacina(),
                vacina.getDataAplicacao(),
                vacina.getProximaDose(),
                linkVacina,
                linkPet
        );
    }
}