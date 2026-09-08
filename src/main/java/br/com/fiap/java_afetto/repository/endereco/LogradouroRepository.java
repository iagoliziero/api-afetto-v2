package br.com.fiap.java_afetto.repository.endereco;

import br.com.fiap.java_afetto.model.endereco.Logradouro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogradouroRepository extends JpaRepository<Logradouro, UUID> {
    boolean existsByBairro_Id(UUID idBairro);
}
