package br.com.fiap.java_afetto.model.clinica;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "TBL_CLINICA_VETERINARIO")
public class ClinicaVeterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "data_inicio")
    private LocalDate dataInicio;
    @Enumerated(EnumType.STRING)
    @Column(name = "turno")
    private Turno turno;
    @Column(name = "ativo")
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "id_clinica")
    private Clinica clinica;
    @ManyToOne
    @JoinColumn(name = "id_veterinario")
    private Veterinario veterinario;

    public ClinicaVeterinario() {}

    public ClinicaVeterinario(UUID id, Clinica clinica, Veterinario veterinario, LocalDate dataInicio, Turno turno, Boolean ativo) {
        this.id = id;
        this.clinica = clinica;
        this.veterinario = veterinario;
        this.dataInicio = dataInicio;
        this.turno = turno;
        this.ativo = ativo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Clinica getClinica() {
        return clinica;
    }

    public void setClinica(Clinica clinica) {
        this.clinica = clinica;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

}