package br.com.fiap.java_afetto.model.endereco;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TBL_PAIS")
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_pais")
    private UUID id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "sigla")
    private String sigla;

    @OneToMany(mappedBy = "pais")
    private List<Estado> estados;

    public Pais(UUID id, String nome, String sigla, List<Estado> estados) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.estados = estados;
    }

    public Pais() {
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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }
}
