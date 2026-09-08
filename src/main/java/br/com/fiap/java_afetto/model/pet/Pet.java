package br.com.fiap.java_afetto.model.pet;

import br.com.fiap.java_afetto.model.Historico;

import br.com.fiap.java_afetto.model.Usuario;
import br.com.fiap.java_afetto.model.Vacina;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TBL_PET")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "nome")
    private String nome;
    @Enumerated(EnumType.STRING)
    @Column(name = "especie")
    private EspeciePet especie;
    @Column(name = "raca")
    private String raca;
    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private SexoPet sexo;
    @Column(name = "peso")
    private Float peso;
    @Column(name = "data_nasc")
    private LocalDate dataNasc;
    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @JsonIgnore
    @OneToMany(mappedBy = "pet")
    private List<Historico> historicos;
    @JsonIgnore
    @OneToMany(mappedBy = "pet")
    private List<Vacina> vacinas;

    public Pet() {}

    public Pet(UUID id, String nome, EspeciePet especie, String raca, SexoPet sexo, Float peso, LocalDate dataNasc, String descricao, Usuario usuario, List<Historico> historicos, List<Vacina> vacinas) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.sexo = sexo;
        this.peso = peso;
        this.dataNasc = dataNasc;
        this.descricao = descricao;
        this.usuario = usuario;
        this.historicos = historicos;
        this.vacinas = vacinas;
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public EspeciePet getEspecie() { return especie; }

    public void setEspecie(EspeciePet especie) { this.especie = especie; }

    public String getRaca() { return raca; }

    public void setRaca(String raca) { this.raca = raca; }

    public SexoPet getSexo() { return sexo; }

    public void setSexo(SexoPet sexo) { this.sexo = sexo; }

    public Float getPeso() { return peso; }

    public void setPeso(Float peso) { this.peso = peso; }

    public LocalDate getDataNasc() { return dataNasc; }

    public void setDataNasc(LocalDate dataNasc) { this.dataNasc = dataNasc; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Historico> getHistoricos() { return historicos; }

    public void setHistoricos(List<Historico> historicos) { this.historicos = historicos; }

    public List<Vacina> getVacinas() { return vacinas; }

    public void setVacinas(List<Vacina> vacinas) { this.vacinas = vacinas; }
}