package br.com.fiap.java_afetto.service;

import br.com.fiap.java_afetto.dto.vacina.VacinaLista;
import br.com.fiap.java_afetto.dto.vacina.VacinaRequest;
import br.com.fiap.java_afetto.dto.vacina.VacinaResponse;
import br.com.fiap.java_afetto.mapper.VacinaMapper;
import br.com.fiap.java_afetto.model.Historico;
import br.com.fiap.java_afetto.model.pet.Pet;
import br.com.fiap.java_afetto.model.Vacina ;
import br.com.fiap.java_afetto.repository.HistoricoRepository;
import br.com.fiap.java_afetto.repository.PetRepository;
import br.com.fiap.java_afetto.repository.VacinaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VacinaService {

    private final VacinaRepository vacinaRepository;
    private final VacinaMapper vacinaMapper;
    private final PetRepository petRepository;
    private final HistoricoRepository historicoRepository;

    public VacinaService(
            VacinaRepository vacinaRepository,
            VacinaMapper vacinaMapper,
            PetRepository petRepository,
            HistoricoRepository historicoRepository
    ) {
        this.vacinaRepository = vacinaRepository;
        this.vacinaMapper = vacinaMapper;
        this.petRepository = petRepository;
        this.historicoRepository = historicoRepository;
    }

    public VacinaResponse create(VacinaRequest request) {

        Pet pet = petRepository
                .findById(request.idPet())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Pet não encontrado"
                        )
                );

        Vacina vacina = new Vacina();

        vacina.setNomeVacina(request.nomeVacina());
        vacina.setFabricante(request.fabricante());
        vacina.setLote(request.lote());
        vacina.setDataAplicacao(request.dataAplicacao());
        vacina.setProximaDose(request.proximaDose());
        vacina.setObservacoes(request.observacoes());
        vacina.setPet(pet);

        if (request.idHistorico() != null) {

            Historico historico = historicoRepository
                    .findById(request.idHistorico())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Histórico não encontrado"
                            )
                    );

            vacina.setHistorico(historico);
        }

        return vacinaMapper.vacinaToResponse(
                vacinaRepository.save(vacina)
        );
    }

    public VacinaResponse read(UUID id) {

        Vacina vacina = vacinaRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Vacina não encontrada"
                        )
                );

        return vacinaMapper.vacinaToResponse(vacina);
    }

    public Page<VacinaLista> read(Pageable pageable) {

        return vacinaRepository
                .findAll(pageable)
                .map(vacinaMapper::vacinaToResponseLista);
    }

    public VacinaResponse update(
            UUID id,
            VacinaRequest request
    ) {

        Vacina vacina = vacinaRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Vacina não encontrada"
                        )
                );

        Pet pet = petRepository
                .findById(request.idPet())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Pet não encontrado"
                        )
                );

        vacina.setNomeVacina(request.nomeVacina());
        vacina.setFabricante(request.fabricante());
        vacina.setLote(request.lote());
        vacina.setDataAplicacao(request.dataAplicacao());
        vacina.setProximaDose(request.proximaDose());
        vacina.setObservacoes(request.observacoes());
        vacina.setPet(pet);

        if (request.idHistorico() != null) {

            Historico historico = historicoRepository
                    .findById(request.idHistorico())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Histórico não encontrado"
                            )
                    );

            vacina.setHistorico(historico);

        } else {

            vacina.setHistorico(null);
        }

        return vacinaMapper.vacinaToResponse(
                vacinaRepository.save(vacina)
        );
    }

    public void delete(UUID id) {

        if (!vacinaRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Vacina não encontrada"
            );
        }

        vacinaRepository.deleteById(id);
    }
}