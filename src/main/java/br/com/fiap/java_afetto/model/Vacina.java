package br.com.fiap.java_afetto.model;

import br.com.fiap.java_afetto.model.Historico;
import br.com.fiap.java_afetto.model.pet.Pet;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "TBL_VACINA")
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_vacina")
    private UUID id;
    @Column(name = "nome_vacina", nullable = false, length = 100)
    private String nomeVacina;
    @Column(name = "fabricante", length = 100)
    private String fabricante;
    @Column(name = "lote", length = 50)
    private String lote;
    @Column(name = "data_aplicacao", nullable = false)
    private LocalDate dataAplicacao;
    @Column(name = "proxima_dose")
    private LocalDate proximaDose;
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "id_pet")
    private Pet pet;
    @ManyToOne
    @JoinColumn(name = "id_historico", nullable = true)
    private Historico historico;


    public Vacina() {}

    public Vacina(UUID id, Pet pet, Historico historico, String nomeVacina, String fabricante, String lote, LocalDate dataAplicacao, LocalDate proximaDose, String observacoes) {
        this.id = id;
        this.pet = pet;
        this.historico = historico;
        this.nomeVacina = nomeVacina;
        this.fabricante = fabricante;
        this.lote = lote;
        this.dataAplicacao = dataAplicacao;
        this.proximaDose = proximaDose;
        this.observacoes = observacoes;
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public Pet getPet() { return pet; }

    public void setPet(Pet pet) { this.pet = pet; }

    public Historico getHistorico() { return historico; }

    public void setHistorico(Historico historico) { this.historico = historico; }

    public String getNomeVacina() { return nomeVacina; }

    public void setNomeVacina(String nomeVacina) { this.nomeVacina = nomeVacina; }

    public String getFabricante() { return fabricante; }

    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public String getLote() { return lote; }

    public void setLote(String lote) { this.lote = lote; }

    public LocalDate getDataAplicacao() { return dataAplicacao; }

    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }

    public LocalDate getProximaDose() { return proximaDose; }

    public void setProximaDose(LocalDate proximaDose) { this.proximaDose = proximaDose; }

    public String getObservacoes() { return observacoes; }

    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}