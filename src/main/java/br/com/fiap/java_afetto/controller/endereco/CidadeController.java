package br.com.fiap.java_afetto.controller.endereco;

import br.com.fiap.java_afetto.dto.cidade.CidadeLista;
import br.com.fiap.java_afetto.dto.cidade.CidadeRequest;
import br.com.fiap.java_afetto.dto.cidade.CidadeResponse;
import br.com.fiap.java_afetto.service.endereco.CidadeService;
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
@RequestMapping("/cidade")
@Tag(name = "CRUD-CIDADES")
public class CidadeController {

    private final CidadeService cidadeService;

    public CidadeController(CidadeService cidadeService){
        this.cidadeService = cidadeService;
    }

    @Operation(summary = "Cria uma cidade")
    @PostMapping
    public ResponseEntity<CidadeResponse> createCidade(@Valid @RequestBody CidadeRequest cidadeRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(cidadeService.create(cidadeRequest));
    }

    @Operation(summary = "Busca uma cidade por id")
    @GetMapping("/{id}")
    public ResponseEntity<CidadeResponse> readCidade(@PathVariable UUID id) {
        CidadeResponse cidade = cidadeService.read(id);
        if (cidade == null) {
            return new ResponseEntity<>(cidade, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(cidade, HttpStatus.OK);
    }

    @Operation(summary = "Busca todos as cidades por página")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Página de cidades retornada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CidadeLista.class))),
            @ApiResponse(responseCode = "404",
                    description = "Nenhuma cidade encontrado!",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping
    public ResponseEntity<Page<CidadeLista>> readCidade(@RequestParam(defaultValue = "0") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20, Sort.by("nome").ascending());
        Page<CidadeLista> cidade = cidadeService.read(pageable);
        if (cidade.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cidade, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza a cidade")
    @PutMapping("/{id}")
    public ResponseEntity<CidadeResponse> updateCidade(
            @PathVariable UUID id,
            @Valid @RequestBody CidadeRequest request) {
        return ResponseEntity.ok(cidadeService.update(id, request));
    }

    @Operation(summary = "Deleta a cidade")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCidade(@PathVariable UUID id) {
        cidadeService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
