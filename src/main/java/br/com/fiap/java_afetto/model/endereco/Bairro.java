package br.com.fiap.java_afetto.model.endereco;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TBL_BAIRRO")
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_bairro")
    private UUID id;
    @Column(name = "nome")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_cidade", nullable = false)
    private Cidade cidade;
    @OneToMany(mappedBy = "bairro")
    private List<Logradouro> logradouros;

    public Bairro(UUID id, String nome, Cidade cidade, List<Logradouro> logradouros) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.logradouros = logradouros;
    }

    public Bairro() {
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

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public List<Logradouro> getLogradouros() {
        return logradouros;
    }

    public void setLogradouros(List<Logradouro> logradouros) {
        this.logradouros = logradouros;
    }
}
