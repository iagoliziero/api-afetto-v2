package br.com.fiap.java_afetto.controller.endereco;

import br.com.fiap.java_afetto.dto.logradouro.LogradouroLista;
import br.com.fiap.java_afetto.dto.logradouro.LogradouroRequest;
import br.com.fiap.java_afetto.dto.logradouro.LogradouroResponse;
import br.com.fiap.java_afetto.service.endereco.LogradouroService;
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
@RequestMapping("/logradouro")
@Tag(name = "CRUD-LOGRADOUROS")
public class LogradouroController {

    private final LogradouroService logradouroService;

    public LogradouroController(LogradouroService logradouroService){
        this.logradouroService = logradouroService;
    }

    @Operation(summary = "Cria um logradouro")
    @PostMapping
    public ResponseEntity<LogradouroResponse> createLogradouro(@Valid @RequestBody LogradouroRequest logradouroRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(logradouroService.create(logradouroRequest));
    }

    @Operation(summary = "Busca um logradouro por id")
    @GetMapping("/{id}")
    public ResponseEntity<LogradouroResponse> readLogradouro(@PathVariable UUID id) {
        LogradouroResponse logradouro = logradouroService.read(id);
        if (logradouro == null) {
            return new ResponseEntity<>(logradouro, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(logradouro, HttpStatus.OK);
    }

    @Operation(summary = "Busca todos os logradouros por página")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Página de logradouros retornada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LogradouroLista.class))),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum logradouro encontrado!",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping
    public ResponseEntity<Page<LogradouroLista>> readLogradouro(@RequestParam(defaultValue = "0") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20, Sort.by("nome").ascending());
        Page<LogradouroLista> logradouro = logradouroService.read(pageable);
        if (logradouro.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(logradouro, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza o logradouro")
    @PutMapping("/{id}")
    public ResponseEntity<LogradouroResponse> updateLogradouro(
            @PathVariable UUID id,
            @Valid @RequestBody LogradouroRequest request) {
        return ResponseEntity.ok(logradouroService.update(id, request));
    }

    @Operation(summary = "Deleta o logradouro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogradouro(@PathVariable UUID id) {
        logradouroService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
