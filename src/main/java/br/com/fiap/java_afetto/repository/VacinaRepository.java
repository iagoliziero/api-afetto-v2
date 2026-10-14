package br.com.fiap.java_afetto.repository;

import br.com.fiap.java_afetto.model.Vacina;
import br.com.fiap.java_afetto.model.pet.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VacinaRepository extends JpaRepository<Vacina, UUID> {

    Page<Vacina> findByPet_Id(UUID idPet, Pageable pageable);
}
