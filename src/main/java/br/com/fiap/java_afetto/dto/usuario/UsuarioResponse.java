package br.com.fiap.java_afetto.dto.usuario;

import org.springframework.hateoas.Link;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record UsuarioResponse(UUID id, String nome, String cpf, String email, LocalDate dataNascimento, String telefone, Link linkEndereco) {
}
