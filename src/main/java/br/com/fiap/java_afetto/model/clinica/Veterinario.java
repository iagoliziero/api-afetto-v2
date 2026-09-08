package br.com.fiap.java_afetto.model.clinica;
import br.com.fiap.java_afetto.model.Historico;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TBL_VETERINARIO")
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_veterinario")
    private UUID id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "especialidade")
    private String especialidade;
    @Column(name = "crmv")
    private String crmv;
    @Column(name = "telefone")
    private String telefone;

    @JsonIgnore
    @OneToMany(mappedBy = "veterinario")
    private List<ClinicaVeterinario> clinicas;
    @JsonIgnore
    @OneToMany(mappedBy = "veterinario")
    private List<Historico> historicos;

    public Veterinario() {}

    public Veterinario(UUID id, String nome, String especialidade, String crmv, String telefone, List<ClinicaVeterinario> clinicas, List<Historico> historicos) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.crmv = crmv;
        this.telefone = telefone;
        this.clinicas = clinicas;
        this.historicos = historicos;
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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<ClinicaVeterinario> getClinicas() {
        return clinicas;
    }

    public void setClinicas(List<ClinicaVeterinario> clinicas) {
        this.clinicas = clinicas;
    }

    public List<Historico> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(List<Historico> historicos) {
        this.historicos = historicos;
    }
}