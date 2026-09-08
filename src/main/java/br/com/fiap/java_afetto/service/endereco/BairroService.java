package br.com.fiap.java_afetto.service.endereco;


import br.com.fiap.java_afetto.dto.bairro.BairroLista;
import br.com.fiap.java_afetto.dto.bairro.BairroRequest;
import br.com.fiap.java_afetto.dto.bairro.BairroResponse;
import br.com.fiap.java_afetto.mapper.BairroMapper;
import br.com.fiap.java_afetto.model.endereco.Bairro;
import br.com.fiap.java_afetto.model.endereco.Cidade;
import br.com.fiap.java_afetto.repository.endereco.BairroRepository;
import br.com.fiap.java_afetto.repository.endereco.CidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BairroService {

    private final BairroRepository bairroRepository;
    private final BairroMapper bairroMapper;
    private final CidadeRepository cidadeRepository;

    @Autowired
    public BairroService(BairroRepository bairroRepository, BairroMapper bairroMapper, CidadeRepository cidadeRepository) {
        this.bairroRepository = bairroRepository;
        this.bairroMapper = bairroMapper;
        this.cidadeRepository = cidadeRepository;
    }


    public BairroResponse create(BairroRequest bairroRequest) {
        Cidade cidade = cidadeRepository.findById(bairroRequest.idCidade())
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada"));

        Bairro bairro = new Bairro();
        bairro.setNome(bairroRequest.nome());
        bairro.setCidade(cidade);

        return bairroMapper.bairroToResponse(bairroRepository.save(bairro));
    }

    public BairroResponse read(UUID id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bairro não encontrada"));
        return bairroMapper.bairroToResponse(bairro);
    }

    public Page<BairroLista> read(Pageable pageable) {
        return bairroRepository
                .findAll(pageable)
                .map(bairroMapper::bairroToResponseLista);
    }

    public BairroResponse update(UUID id, BairroRequest bairroRequest) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bairro não encontrada"));
        BeanUtils.copyProperties(bairroRequest, bairro);
        return bairroMapper.bairroToResponse(bairroRepository.save(bairro));
    }

    public void delete(UUID id) {
        if (!bairroRepository.existsById(id)) {
            throw new EntityNotFoundException("Bairro não encontrada");
        }
        bairroRepository.deleteById(id);
    }
    
}
