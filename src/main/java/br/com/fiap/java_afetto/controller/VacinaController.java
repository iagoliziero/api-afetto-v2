package br.com.fiap.java_afetto.controller;

import br.com.fiap.java_afetto.dto.vacina.VacinaLista;
import br.com.fiap.java_afetto.dto.vacina.VacinaRequest;
import br.com.fiap.java_afetto.dto.vacina.VacinaResponse;
import br.com.fiap.java_afetto.service.VacinaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/vacina")
public class VacinaController {

    private final VacinaService vacinaService;

    public VacinaController(VacinaService vacinaService) {
        this.vacinaService = vacinaService;
    }

    @PostMapping
    public ResponseEntity<VacinaResponse> createVacina(
            @Valid @RequestBody VacinaRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vacinaService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacinaResponse> readVacina(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                vacinaService.read(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<VacinaLista>> readVacinas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                vacinaService.read(pageable)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacinaResponse> updateVacina(
            @PathVariable UUID id,
            @Valid @RequestBody VacinaRequest request
    ) {

        return ResponseEntity.ok(
                vacinaService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacina(
            @PathVariable UUID id
    ) {

        vacinaService.delete(id);

        return ResponseEntity.noContent().build();
    }
}