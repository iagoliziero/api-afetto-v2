package br.com.fiap.java_afetto.service;

import br.com.fiap.java_afetto.model.Usuario;
import br.com.fiap.java_afetto.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class DistanciaService {

    private static final int RAIO_TERRA = 6371; // km

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public DistanciaService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public double calcularDistancia(UUID idUsuario1, UUID idUsuario2) {
        Usuario usuario1 = usuarioRepository.findById(idUsuario1)
                .orElseThrow(() -> new EntityNotFoundException("Usuário 1 não encontrado"));

        Usuario usuario2 = usuarioRepository.findById(idUsuario2)
                .orElseThrow(() -> new EntityNotFoundException("Usuário 2 não encontrado"));

        if (usuario1.getEndereco() == null || usuario2.getEndereco() == null) {
            throw new IllegalStateException("Ambos os usuários precisam ter endereço cadastrado");
        }

        return calcular(
                usuario1. getEndereco().getLatitude(),
                usuario1.getEndereco().getLongitude(),
                usuario2.getEndereco().getLatitude(),
                usuario2.getEndereco().getLongitude()
        );
    }

    private double calcular(BigDecimal lat1, BigDecimal lon1,
                            BigDecimal lat2, BigDecimal lon2) {
        double dLat = Math.toRadians(lat2.subtract(lat1).doubleValue());
        double dLon = Math.toRadians(lon2.subtract(lon1).doubleValue());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1.doubleValue()))
                * Math.cos(Math.toRadians(lat2.doubleValue()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RAIO_TERRA * c;
    }


}
