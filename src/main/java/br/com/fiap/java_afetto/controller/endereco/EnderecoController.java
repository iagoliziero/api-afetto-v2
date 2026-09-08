package br.com.fiap.java_afetto.controller.endereco;

import br.com.fiap.java_afetto.dto.endereco.EnderecoLista;
import br.com.fiap.java_afetto.dto.endereco.EnderecoRequest;
import br.com.fiap.java_afetto.dto.endereco.EnderecoResponse;
import br.com.fiap.java_afetto.service.endereco.EnderecoService;
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
@RequestMapping("/endereco")
@Tag(name = "CRUD-ENDERECOS")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService){
        this.enderecoService = enderecoService;
    }

    @Operation(summary = "Cria um endereco")
    @PostMapping
    public ResponseEntity<EnderecoResponse> createEndereco(@Valid @RequestBody EnderecoRequest enderecoRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.create(enderecoRequest));
    }

    @Operation(summary = "Busca um endereco por id")
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponse> readEndereco(@PathVariable UUID id) {
        EnderecoResponse endereco = enderecoService.read(id);
        if (endereco == null) {
            return new ResponseEntity<>(endereco, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(endereco, HttpStatus.OK);
    }

    @Operation(summary = "Busca todos os enderecos por página")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Página de enderecos retornada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoLista.class))),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum endereco encontrado!",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping
    public ResponseEntity<Page<EnderecoLista>> readEndereco(@RequestParam(defaultValue = "0") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20);
        Page<EnderecoLista> endereco = enderecoService.read(pageable);
        if (endereco.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(endereco, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza o endereco")
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponse> updateEndereco(
            @PathVariable UUID id,
            @Valid @RequestBody EnderecoRequest request) {
        return ResponseEntity.ok(enderecoService.update(id, request));
    }

    @Operation(summary = "Deleta o endereco")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndereco(@PathVariable UUID id) {
        enderecoService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
