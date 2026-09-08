package br.com.fiap.java_afetto.service.endereco;

import br.com.fiap.java_afetto.dto.pais.PaisLista;
import br.com.fiap.java_afetto.dto.pais.PaisRequest;
import br.com.fiap.java_afetto.dto.pais.PaisResponse;
import br.com.fiap.java_afetto.mapper.PaisMapper;
import br.com.fiap.java_afetto.model.endereco.Pais;
import br.com.fiap.java_afetto.repository.endereco.EstadoRepository;
import br.com.fiap.java_afetto.repository.endereco.PaisRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaisService {
    private final PaisRepository paisRepository;
    private final PaisMapper paisMapper;
    private final EstadoRepository estadoRepository;

    @Autowired
    public PaisService(PaisRepository paisRepository, PaisMapper paisMapper, EstadoRepository estadoRepository) {
        this.paisRepository = paisRepository;
        this.paisMapper = paisMapper;
        this.estadoRepository = estadoRepository;
    }


    public PaisResponse create(PaisRequest paisRequest){
        Pais pais = new Pais();
        BeanUtils.copyProperties(paisRequest, pais);
        return paisMapper.paisToResponse(paisRepository.save(pais));
    }

    public PaisResponse read(UUID id) {
        Pais pais = paisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("País não encontrado"));
        return paisMapper.paisToResponse(pais);
    }

    public Page<PaisLista> read(Pageable pageable) {
        return paisRepository
                .findAll(pageable)
                .map(paisMapper::paisToResponseLista);
    }

    public PaisResponse update(UUID id, PaisRequest request) {
        Pais pais = paisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("País não encontrado"));
        BeanUtils.copyProperties(request, pais);
        return paisMapper.paisToResponse(paisRepository.save(pais));
    }

    public void delete(UUID id) {
        if (!paisRepository.existsById(id)) {
            throw new EntityNotFoundException("País não encontrado");
        }
        if (estadoRepository.existsByPais_Id(id)) {
            throw new DataIntegrityViolationException(
                    "Não é possível excluir um país que possui estados cadastrados"
            );
        }
        paisRepository.deleteById(id);
    }

}
