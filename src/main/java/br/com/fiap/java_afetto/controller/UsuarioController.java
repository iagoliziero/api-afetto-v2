package br.com.fiap.java_afetto.controller;

import br.com.fiap.java_afetto.dto.endereco.EnderecoLista;
import br.com.fiap.java_afetto.dto.usuario.UsuarioLista;
import br.com.fiap.java_afetto.dto.usuario.UsuarioRequest;
import br.com.fiap.java_afetto.dto.usuario.UsuarioResponse;
import br.com.fiap.java_afetto.dto.usuario.UsuarioRoleRequest;
import br.com.fiap.java_afetto.service.DistanciaService;
import br.com.fiap.java_afetto.service.UsuarioService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
@Tag(name = "CRUD-USUARIOS")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final DistanciaService distanciaService;


    public UsuarioController(UsuarioService usuarioService, DistanciaService distanciaService) {
        this.usuarioService = usuarioService;
        this.distanciaService = distanciaService;
    }

    @Operation(summary = "Cria um usuario")
    @PostMapping
    public ResponseEntity<UsuarioResponse> createUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.create(usuarioRequest));
    }

    @Operation(summary = "Busca um usuário por id")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> readUsuario(@PathVariable UUID id) {
        UsuarioResponse usuario = usuarioService.read(id);
        if (usuario == null) {
            return new ResponseEntity<>(usuario, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @Operation(summary = "Busca todos os usuários por página")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Página de usuários retornada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnderecoLista.class))),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum usuário encontrado!",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping
    public ResponseEntity<Page<UsuarioLista>> readUsuario(@RequestParam(defaultValue = "0") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 50);
        Page<UsuarioLista> usuario = usuarioService.read(pageable);
        if (usuario.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza o usuário")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> updateUsuario(
            @PathVariable UUID id,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.update(id, request));
    }

    @Operation(summary = "Deleta o usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable UUID id) {
        usuarioService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Calcula distância entre dois usuários")
    @GetMapping("/distancia")
    public ResponseEntity<Map<String, Object>> calcularDistancia(
            @RequestParam UUID idUsuario1,
            @RequestParam UUID idUsuario2) {

        double distancia = distanciaService.calcularDistancia(idUsuario1, idUsuario2);

        Map<String, Object> response = new HashMap<>();
        response.put("distanciaKm", String.format("%.2f km", distancia));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Troca a role do usuário")
    @PatchMapping("/{id}/role")
    public ResponseEntity<UsuarioResponse> alterarRole(
            @PathVariable UUID id,
            @Valid @RequestBody UsuarioRoleRequest request
    ) {

        UsuarioResponse usuario = usuarioService.alterarRole(id, request);

        return ResponseEntity.ok(usuario);
    }
}
