package br.com.fiap.java_afetto.dto.usuario;

import br.com.fiap.java_afetto.model.UserRole;
import org.springframework.hateoas.Link;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public record UsuarioLista(String nome, String telefone, UserRole role, Link linkUsuario, Link linkEndereco) {
}
