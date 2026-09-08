package br.com.fiap.java_afetto.model.endereco;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TBL_LOGRADOURO")
public class Logradouro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_logradouro")
    private UUID id;
    @Column(name = "nome")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_bairro", nullable = false)
    private Bairro bairro;
    @OneToMany(mappedBy = "logradouro")
    private List<Endereco> enderecos;

    public Logradouro(UUID id, String nome, Bairro bairro, List<Endereco> enderecos) {
        this.id = id;
        this.nome = nome;
        this.bairro = bairro;
        this.enderecos = enderecos;
    }

    public Logradouro() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }
}
