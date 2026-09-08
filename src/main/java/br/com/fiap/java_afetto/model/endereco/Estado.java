package br.com.fiap.java_afetto.model.endereco;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TBL_ESTADO")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_estado")
    private UUID id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "sigla")
    private String sigla;

    @ManyToOne
    @JoinColumn(name = "id_pais", nullable = false)
    private Pais pais;
    @OneToMany(mappedBy = "estado")
    private List<Cidade> cidades;

    public Estado(UUID id, String nome, String sigla, Pais pais, List<Cidade> cidades) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.pais = pais;
        this.cidades = cidades;
    }

    public Estado() {
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

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }
}
