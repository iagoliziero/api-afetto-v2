package br.com.fiap.java_afetto.controller.endereco;


import br.com.fiap.java_afetto.dto.estado.EstadoLista;
import br.com.fiap.java_afetto.dto.estado.EstadoRequest;
import br.com.fiap.java_afetto.dto.estado.EstadoResponse;
import br.com.fiap.java_afetto.model.endereco.Estado;
import br.com.fiap.java_afetto.service.endereco.EstadoService;
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
@RequestMapping("/estado")
@Tag(name = "CRUD-ESTADOS")
public class EstadoController {

    private final EstadoService estadoService;

    public EstadoController(EstadoService estadoService){
        this.estadoService = estadoService;
    }

    @Operation(summary = "Cria um estado")
    @PostMapping
    public ResponseEntity<EstadoResponse> createEstado(@Valid @RequestBody EstadoRequest estadoRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.create(estadoRequest));
    }

    @Operation(summary = "Busca um estado por id")
    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponse> readEstado(@PathVariable UUID id) {
        EstadoResponse estado = estadoService.read(id);
        if (estado == null) {
            return new ResponseEntity<>(estado, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(estado, HttpStatus.OK);
    }

    @Operation(summary = "Busca todos os estados por página")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Página de estados retornada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstadoLista.class))),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum estado encontrado!",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping
    public ResponseEntity<Page<EstadoLista>> readEstado(@RequestParam(defaultValue = "0") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20, Sort.by("nome").ascending());
        Page<EstadoLista> estado = estadoService.read(pageable);
        if (estado.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(estado, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza o estado")
    @PutMapping("/{id}")
    public ResponseEntity<EstadoResponse> updateEstado(
            @PathVariable UUID id,
            @Valid @RequestBody EstadoRequest request) {
        return ResponseEntity.ok(estadoService.update(id, request));
    }

    @Operation(summary = "Deleta o estado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstado(@PathVariable UUID id) {
        estadoService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
