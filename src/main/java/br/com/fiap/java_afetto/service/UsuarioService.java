package br.com.fiap.java_afetto.service;

import br.com.fiap.java_afetto.dto.usuario.UsuarioLista;
import br.com.fiap.java_afetto.dto.usuario.UsuarioRequest;
import br.com.fiap.java_afetto.dto.usuario.UsuarioResponse;
import br.com.fiap.java_afetto.dto.usuario.UsuarioRoleRequest;
import br.com.fiap.java_afetto.mapper.UsuarioMapper;
import br.com.fiap.java_afetto.model.UserRole;
import br.com.fiap.java_afetto.model.Usuario;
import br.com.fiap.java_afetto.model.endereco.Endereco;
import br.com.fiap.java_afetto.repository.UsuarioRepository;
import br.com.fiap.java_afetto.repository.endereco.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final EnderecoRepository enderecoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            EnderecoRepository enderecoRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.enderecoRepository = enderecoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse create(UsuarioRequest usuarioRequest) {

        Usuario usuario = new Usuario();

        usuario.setNome(usuarioRequest.nome());
        usuario.setCpf(usuarioRequest.cpf());
        usuario.setDataNascimento(usuarioRequest.dataNascimento());
        usuario.setEmail(usuarioRequest.email());

        usuario.setSenha(
                passwordEncoder.encode(usuarioRequest.senha())
        );

        usuario.setTelefone(usuarioRequest.telefone());

        usuario.setRole(UserRole.USER);

        if (usuarioRequest.idEndereco() != null) {

            Endereco endereco = enderecoRepository
                    .findById(usuarioRequest.idEndereco())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Endereço não encontrado"
                            )
                    );

            usuario.setEndereco(endereco);
        }

        return usuarioMapper.usuarioToResponse(
                usuarioRepository.save(usuario)
        );
    }

    public UsuarioResponse read(UUID id) {

        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Usuário não encontrado"
                        )
                );

        return usuarioMapper.usuarioToResponse(usuario);
    }

    public Page<UsuarioLista> read(Pageable pageable) {

        return usuarioRepository
                .findAll(pageable)
                .map(usuarioMapper::usuarioToResponseLista);
    }

    public UsuarioResponse update(
            UUID id,
            UsuarioRequest request
    ) {

        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Usuário não encontrado"
                        )
                );

        usuario.setNome(request.nome());
        usuario.setCpf(request.cpf());
        usuario.setEmail(request.email());
        usuario.setTelefone(request.telefone());
        usuario.setDataNascimento(request.dataNascimento());

        usuario.setSenha(
                passwordEncoder.encode(request.senha())
        );

        if (request.idEndereco() != null) {

            Endereco endereco = enderecoRepository
                    .findById(request.idEndereco())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Endereço não encontrado"
                            )
                    );

            usuario.setEndereco(endereco);
        }

        return usuarioMapper.usuarioToResponse(
                usuarioRepository.save(usuario)
        );
    }

    public void delete(UUID id) {

        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Usuário não encontrado"
            );
        }

        usuarioRepository.deleteById(id);
    }

    public UsuarioResponse alterarRole(UUID id, UsuarioRoleRequest request) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuário não encontrado")
                );

        usuario.setRole(request.role());

        return usuarioMapper.usuarioToResponse(
                usuarioRepository.save(usuario)
        );
    }
}