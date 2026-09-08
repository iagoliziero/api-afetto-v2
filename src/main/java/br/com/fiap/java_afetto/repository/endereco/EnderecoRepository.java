package br.com.fiap.java_afetto.repository.endereco;

import br.com.fiap.java_afetto.model.endereco.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {
    boolean existsByLogradouro_Id(UUID idLogradouro);
}
