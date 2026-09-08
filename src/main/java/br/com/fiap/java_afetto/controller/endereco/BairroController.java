package br.com.fiap.java_afetto.controller.endereco;

import br.com.fiap.java_afetto.dto.bairro.BairroLista;
import br.com.fiap.java_afetto.dto.bairro.BairroRequest;
import br.com.fiap.java_afetto.dto.bairro.BairroResponse;
import br.com.fiap.java_afetto.service.endereco.BairroService;
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
@RequestMapping("/bairro")
@Tag(name = "CRUD-BAIRROS")
public class BairroController {

    private final BairroService bairroService;

    public BairroController(BairroService bairroService){
        this.bairroService = bairroService;
    }

    @Operation(summary = "Cria um bairro")
    @PostMapping
    public ResponseEntity<BairroResponse> createBairro(@Valid @RequestBody BairroRequest bairroRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(bairroService.create(bairroRequest));
    }

    @Operation(summary = "Busca um bairro por id")
    @GetMapping("/{id}")
    public ResponseEntity<BairroResponse> readBairro(@PathVariable UUID id) {
        BairroResponse bairro = bairroService.read(id);
        if (bairro == null) {
            return new ResponseEntity<>(bairro, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bairro, HttpStatus.OK);
    }

    @Operation(summary = "Busca todos as bairros por página")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Página de bairros retornada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BairroLista.class))),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum bairro encontrado!",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping
    public ResponseEntity<Page<BairroLista>> readBairro(@RequestParam(defaultValue = "0") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20, Sort.by("nome").ascending());
        Page<BairroLista> bairro = bairroService.read(pageable);
        if (bairro.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bairro, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza a bairro")
    @PutMapping("/{id}")
    public ResponseEntity<BairroResponse> updateBairro(
            @PathVariable UUID id,
            @Valid @RequestBody BairroRequest request) {
        return ResponseEntity.ok(bairroService.update(id, request));
    }

    @Operation(summary = "Deleta a bairro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBairro(@PathVariable UUID id) {
        bairroService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
