package br.com.fiap.java_afetto.service.endereco;

import br.com.fiap.java_afetto.dto.logradouro.LogradouroLista;
import br.com.fiap.java_afetto.dto.logradouro.LogradouroRequest;
import br.com.fiap.java_afetto.dto.logradouro.LogradouroResponse;
import br.com.fiap.java_afetto.mapper.LogradouroMapper;
import br.com.fiap.java_afetto.model.endereco.Logradouro;
import br.com.fiap.java_afetto.model.endereco.Bairro;
import br.com.fiap.java_afetto.repository.endereco.LogradouroRepository;
import br.com.fiap.java_afetto.repository.endereco.BairroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class LogradouroService {

    private final LogradouroRepository logradouroRepository;
    private final LogradouroMapper logradouroMapper;
    private final BairroRepository bairroRepository;

    @Autowired
    public LogradouroService(LogradouroRepository logradouroRepository, LogradouroMapper logradouroMapper, BairroRepository bairroRepository) {
        this.logradouroRepository = logradouroRepository;
        this.logradouroMapper = logradouroMapper;
        this.bairroRepository = bairroRepository;
    }


    public LogradouroResponse create(LogradouroRequest logradouroRequest) {
        Bairro bairro = bairroRepository.findById(logradouroRequest.idBairro())
                .orElseThrow(() -> new EntityNotFoundException("Bairro não encontrado"));

        Logradouro logradouro = new Logradouro();
        logradouro.setNome(logradouroRequest.nome());
        logradouro.setBairro(bairro);

        return logradouroMapper.logradouroToResponse(logradouroRepository.save(logradouro));
    }

    public LogradouroResponse read(UUID id) {
        Logradouro logradouro = logradouroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Logradouro não encontrado"));
        return logradouroMapper.logradouroToResponse(logradouro);
    }

    public Page<LogradouroLista> read(Pageable pageable) {
        return logradouroRepository
                .findAll(pageable)
                .map(logradouroMapper::logradouroToResponseLista);
    }

    public LogradouroResponse update(UUID id, LogradouroRequest logradouroRequest) {
        Logradouro logradouro = logradouroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Logradouro não encontrado"));
        BeanUtils.copyProperties(logradouroRequest, logradouro);
        return logradouroMapper.logradouroToResponse(logradouroRepository.save(logradouro));
    }

    public void delete(UUID id) {
        if (!logradouroRepository.existsById(id)) {
            throw new EntityNotFoundException("Logradouro não encontrado");
        }
        logradouroRepository.deleteById(id);
    }

}
