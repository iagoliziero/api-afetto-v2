package br.com.fiap.java_afetto.controller;

import br.com.fiap.java_afetto.dto.pet.PetLista;
import br.com.fiap.java_afetto.dto.pet.PetRequest;
import br.com.fiap.java_afetto.dto.pet.PetResponse;
import br.com.fiap.java_afetto.dto.pet.PetStatusVacinacaoResponse;
import br.com.fiap.java_afetto.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }


    @PostMapping
    @Operation(summary = "Cria o pet")
    public ResponseEntity<PetResponse> createPet(
            @Valid @RequestBody PetRequest petRequest
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(petService.create(petRequest));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Exibe o pet")
    public ResponseEntity<PetResponse> readPet(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                petService.read(id)
        );
    }


    @GetMapping
    @Operation(summary = "Exibe os pet")
    public ResponseEntity<Page<PetLista>> readPets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                petService.read(pageable)
        );
    }


    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o pet")
    public ResponseEntity<PetResponse> updatePet(
            @PathVariable UUID id,
            @Valid @RequestBody PetRequest petRequest
    ) {

        return ResponseEntity.ok(
                petService.update(id, petRequest)
        );
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta o pet")
    public ResponseEntity<Void> deletePet(
            @PathVariable UUID id
    ) {

        petService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/status-vacinacao")
    @Operation(summary = "Verifica o status da vacinacao")
    public ResponseEntity<PetStatusVacinacaoResponse> statusVacinacao(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                petService.statusVacinacao(id)
        );
    }
}