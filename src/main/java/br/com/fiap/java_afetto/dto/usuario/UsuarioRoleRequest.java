package br.com.fiap.java_afetto.dto.usuario;

import br.com.fiap.java_afetto.model.UserRole;
import jakarta.validation.constraints.NotNull;

public record UsuarioRoleRequest(

        @NotNull(message = "A role é obrigatória")
        UserRole role

) {
}