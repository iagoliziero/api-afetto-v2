package br.com.fiap.java_afetto.service.endereco;


import br.com.fiap.java_afetto.dto.estado.EstadoLista;
import br.com.fiap.java_afetto.dto.estado.EstadoRequest;
import br.com.fiap.java_afetto.dto.estado.EstadoResponse;
import br.com.fiap.java_afetto.mapper.EstadoMapper;
import br.com.fiap.java_afetto.model.endereco.Estado;
import br.com.fiap.java_afetto.model.endereco.Pais;
import br.com.fiap.java_afetto.repository.endereco.EstadoRepository;
import br.com.fiap.java_afetto.repository.endereco.PaisRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class EstadoService {

    private final EstadoRepository estadoRepository;
    private final EstadoMapper estadoMapper;
    private final PaisRepository paisRepository;

    @Autowired
    public EstadoService(EstadoRepository estadoRepository, EstadoMapper estadoMapper, PaisRepository paisRepository) {
        this.estadoRepository = estadoRepository;
        this.estadoMapper = estadoMapper;
        this.paisRepository = paisRepository;
    }


    public EstadoResponse create(EstadoRequest request) {
        Pais pais = paisRepository.findById(request.idPais())
                .orElseThrow(() -> new EntityNotFoundException("País não encontrado"));

        Estado estado = new Estado();
        estado.setNome(request.nome());
        estado.setSigla(request.sigla());
        estado.setPais(pais);

        return estadoMapper.estadoToResponse(estadoRepository.save(estado));
    }

    public EstadoResponse read(UUID id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("País não encontrado"));
        return estadoMapper.estadoToResponse(estado);
    }

    public Page<EstadoLista> read(Pageable pageable) {
        return estadoRepository
                .findAll(pageable)
                .map(estadoMapper::estadoToResponseLista);
    }

    public EstadoResponse update(UUID id, EstadoRequest request) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado"));
        BeanUtils.copyProperties(request, estado);
        return estadoMapper.estadoToResponse(estadoRepository.save(estado));
    }

    public void delete(UUID id) {
        if (!estadoRepository.existsById(id)) {
            throw new EntityNotFoundException("Estado não encontrado");
        }
        estadoRepository.deleteById(id);
    }
}
