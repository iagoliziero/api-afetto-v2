package br.com.fiap.java_afetto.controller.endereco;

import br.com.fiap.java_afetto.dto.pais.PaisLista;
import br.com.fiap.java_afetto.dto.pais.PaisRequest;
import br.com.fiap.java_afetto.dto.pais.PaisResponse;
import br.com.fiap.java_afetto.model.endereco.Pais;
import br.com.fiap.java_afetto.service.endereco.PaisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pais")
@Tag(name = "CRUD-PAISES")
public class PaisController {

    private final PaisService paisService;

    public PaisController(PaisService paisService){
        this.paisService = paisService;
    }

    @Operation(summary = "Cria um país")
    @PostMapping
    public ResponseEntity<PaisResponse> createPais(@Valid @RequestBody PaisRequest paisRequest){
        PaisResponse paisSalvo = paisService.create(paisRequest);
        return new ResponseEntity<>(paisSalvo, HttpStatus.CREATED);
    }

    @Operation(summary = "Busca um país por id")
    @GetMapping("/{id}")
    public ResponseEntity<PaisResponse> readPais(@PathVariable UUID id) {
        PaisResponse pais = paisService.read(id);
        if (pais == null) {
            return new ResponseEntity<>(pais, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pais, HttpStatus.OK);
    }

    @Operation(summary = "Busca todos os países por página")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Página de países retornada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaisLista.class))),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum país encontrado!",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping
    public ResponseEntity<Page<PaisLista>> readPais(@RequestParam(defaultValue = "0") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20, Sort.by("nome").ascending());
        Page<PaisLista> pais = paisService.read(pageable);
        if (pais.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pais, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza o país")
    @PutMapping("/{id}")
    public ResponseEntity<PaisResponse> updatePais(
            @PathVariable UUID id,
            @Valid @RequestBody PaisRequest request) {
        return ResponseEntity.ok(paisService.update(id, request));
    }

    @Operation(summary = "Deleta o país")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePais(@PathVariable UUID id) {
        paisService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
