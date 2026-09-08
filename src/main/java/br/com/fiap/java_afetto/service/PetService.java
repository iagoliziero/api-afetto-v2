package br.com.fiap.java_afetto.service;

import br.com.fiap.java_afetto.dto.pet.PetLista;
import br.com.fiap.java_afetto.dto.pet.PetRequest;
import br.com.fiap.java_afetto.dto.pet.PetResponse;
import br.com.fiap.java_afetto.dto.pet.PetStatusVacinacaoResponse;
import br.com.fiap.java_afetto.mapper.PetMapper;
import br.com.fiap.java_afetto.model.Usuario;
import br.com.fiap.java_afetto.model.Vacina;
import br.com.fiap.java_afetto.model.pet.Pet;
import br.com.fiap.java_afetto.repository.PetRepository;
import br.com.fiap.java_afetto.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final UsuarioRepository usuarioRepository;

    public PetService(
            PetRepository petRepository,
            PetMapper petMapper,
            UsuarioRepository usuarioRepository
    ) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public PetResponse create(PetRequest petRequest) {

        Pet pet = new Pet();

        pet.setNome(petRequest.nome());
        pet.setEspecie(petRequest.especie());
        pet.setRaca(petRequest.raca());
        pet.setSexo(petRequest.sexo());
        pet.setPeso(petRequest.peso());
        pet.setDataNasc(petRequest.dataNasc());
        pet.setDescricao(petRequest.descricao());

        if (petRequest.idUsuario() != null) {

            Usuario usuario = usuarioRepository
                    .findById(petRequest.idUsuario())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Usuário não encontrado"
                            )
                    );

            pet.setUsuario(usuario);
        }

        return petMapper.petToResponse(
                petRepository.save(pet)
        );
    }

    public PetResponse read(UUID id) {

        Pet pet = petRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Pet não encontrado"
                        )
                );

        return petMapper.petToResponse(pet);
    }

    public Page<PetLista> read(Pageable pageable) {

        return petRepository
                .findAll(pageable)
                .map(petMapper::petToResponseLista);
    }

    public PetResponse update(
            UUID id,
            PetRequest request
    ) {

        Pet pet = petRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Pet não encontrado"
                        )
                );

        pet.setNome(request.nome());
        pet.setEspecie(request.especie());
        pet.setRaca(request.raca());
        pet.setSexo(request.sexo());
        pet.setPeso(request.peso());
        pet.setDataNasc(request.dataNasc());
        pet.setDescricao(request.descricao());

        if (request.idUsuario() != null) {

            Usuario usuario = usuarioRepository
                    .findById(request.idUsuario())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Usuário não encontrado"
                            )
                    );

            pet.setUsuario(usuario);
        }

        return petMapper.petToResponse(
                petRepository.save(pet)
        );
    }

    public void delete(UUID id) {

        if (!petRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Pet não encontrado"
            );
        }

        petRepository.deleteById(id);
    }

    public PetStatusVacinacaoResponse statusVacinacao(UUID id) {

        Pet pet = petRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Pet não encontrado"
                        )
                );

        LocalDate hoje = LocalDate.now();
        LocalDate limiteProximas = hoje.plusDays(30);

        int atrasadas = 0;
        int proximas = 0;
        int emDia = 0;

        for (Vacina vacina : pet.getVacinas()) {

            if (vacina.getProximaDose() == null) {
                emDia++;
                continue;
            }

            LocalDate proximaDose = vacina.getProximaDose();

            if (proximaDose.isBefore(hoje)) {

                atrasadas++;

            } else if (
                    !proximaDose.isAfter(limiteProximas)
            ) {

                proximas++;

            } else {

                emDia++;
            }
        }

        String status;

        if (atrasadas > 0) {
            status = "ATENCAO";
        } else if (proximas > 0) {
            status = "PROXIMA_DOSE";
        } else {
            status = "EM_DIA";
        }

        return new PetStatusVacinacaoResponse(
                pet.getNome(),
                pet.getVacinas().size(),
                atrasadas,
                proximas,
                emDia,
                status
        );
    }
}