package br.com.fiap.java_afetto.service.endereco;

import br.com.fiap.java_afetto.dto.endereco.EnderecoLista;
import br.com.fiap.java_afetto.dto.endereco.EnderecoRequest;
import br.com.fiap.java_afetto.dto.endereco.EnderecoResponse;
import br.com.fiap.java_afetto.mapper.EnderecoMapper;
import br.com.fiap.java_afetto.model.endereco.Logradouro;
import br.com.fiap.java_afetto.model.endereco.Endereco;
import br.com.fiap.java_afetto.repository.endereco.LogradouroRepository;
import br.com.fiap.java_afetto.repository.endereco.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper enderecoMapper;
    private final LogradouroRepository logradouroRepository;

    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository, EnderecoMapper enderecoMapper, LogradouroRepository logradouroRepository) {
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
        this.logradouroRepository = logradouroRepository;
    }


    public EnderecoResponse create(EnderecoRequest enderecoRequest) {
        Logradouro logradouro = logradouroRepository.findById(enderecoRequest.idLogradouro())
                .orElseThrow(() -> new EntityNotFoundException("Logradouro não encontrado"));

        Endereco endereco = new Endereco();
        endereco.setNumero(enderecoRequest.numero());
        endereco.setComplemento(enderecoRequest.complemento());
        endereco.setCep(enderecoRequest.cep());
        endereco.setLatitude(enderecoRequest.latitude());
        endereco.setLongitude(enderecoRequest.longitude());
        endereco.setLogradouro(logradouro);

        return enderecoMapper.enderecoToResponse(enderecoRepository.save(endereco));
    }

    public EnderecoResponse read(UUID id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado"));
        return enderecoMapper.enderecoToResponse(endereco);
    }

    public Page<EnderecoLista> read(Pageable pageable) {
        return enderecoRepository
                .findAll(pageable)
                .map(enderecoMapper::enderecoToResponseLista);
    }

    public EnderecoResponse update(UUID id, EnderecoRequest enderecoRequest) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado"));
        BeanUtils.copyProperties(enderecoRequest, endereco);
        return enderecoMapper.enderecoToResponse(enderecoRepository.save(endereco));
    }

    public void delete(UUID id) {
        if (!enderecoRepository.existsById(id)) {
            throw new EntityNotFoundException("Endereco não encontrado");
        }
        enderecoRepository.deleteById(id);
    }

}
