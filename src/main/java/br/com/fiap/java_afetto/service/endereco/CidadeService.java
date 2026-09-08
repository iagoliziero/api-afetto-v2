package br.com.fiap.java_afetto.service.endereco;

import br.com.fiap.java_afetto.dto.cidade.CidadeLista;
import br.com.fiap.java_afetto.dto.cidade.CidadeRequest;
import br.com.fiap.java_afetto.dto.cidade.CidadeResponse;
import br.com.fiap.java_afetto.mapper.CidadeMapper;
import br.com.fiap.java_afetto.model.endereco.Cidade;
import br.com.fiap.java_afetto.model.endereco.Estado;
import br.com.fiap.java_afetto.repository.endereco.CidadeRepository;
import br.com.fiap.java_afetto.repository.endereco.EstadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CidadeService {

    private final CidadeRepository cidadeRepository;
    private final CidadeMapper cidadeMapper;
    private final EstadoRepository estadoRepository;

    @Autowired
    public CidadeService(CidadeRepository cidadeRepository, CidadeMapper cidadeMapper, EstadoRepository estadoRepository) {
        this.cidadeRepository = cidadeRepository;
        this.cidadeMapper = cidadeMapper;
        this.estadoRepository = estadoRepository;
    }


    public CidadeResponse create(CidadeRequest cidadeRequest) {
        Estado estado = estadoRepository.findById(cidadeRequest.idEstado())
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrada"));

        Cidade cidade = new Cidade();
        cidade.setNome(cidadeRequest.nome());
        cidade.setEstado(estado);

        return cidadeMapper.cidadeToResponse(cidadeRepository.save(cidade));
    }

    public CidadeResponse read(UUID id) {
        Cidade cidade = cidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada"));
        return cidadeMapper.cidadeToResponse(cidade);
    }

    public Page<CidadeLista> read(Pageable pageable) {
        return cidadeRepository
                .findAll(pageable)
                .map(cidadeMapper::cidadeToResponseLista);
    }

    public CidadeResponse update(UUID id, CidadeRequest cidadeRequest) {
        Cidade cidade = cidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada"));
        BeanUtils.copyProperties(cidadeRequest, cidade);
        return cidadeMapper.cidadeToResponse(cidadeRepository.save(cidade));
    }

    public void delete(UUID id) {
        if (!cidadeRepository.existsById(id)) {
            throw new EntityNotFoundException("Cidade não encontrada");
        }
        cidadeRepository.deleteById(id);
    }

}
