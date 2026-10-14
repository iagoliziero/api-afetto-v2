package br.com.fiap.java_afetto.security;

import br.com.fiap.java_afetto.model.Usuario;
import br.com.fiap.java_afetto.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Descobre quem está logado a partir da sessão (o e-mail usado no POST /login).
 * Os endpoints novos usam isto em vez de receber o idUsuario no corpo.
 */
@Service
public class UsuarioLogadoService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioLogadoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario buscarUsuarioLogado() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new EntityNotFoundException(
                    "Usuário não encontrado"
            );
        }

        return usuarioRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Usuário não encontrado"
                        )
                );
    }
}
