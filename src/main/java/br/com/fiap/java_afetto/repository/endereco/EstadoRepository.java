package br.com.fiap.java_afetto.repository.endereco;

import br.com.fiap.java_afetto.model.endereco.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EstadoRepository extends JpaRepository<Estado, UUID> {
    boolean existsByPais_Id(UUID idPais);
}
