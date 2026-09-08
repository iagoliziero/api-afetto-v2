package br.com.fiap.java_afetto.repository;

import br.com.fiap.java_afetto.model.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, UUID> {
}
