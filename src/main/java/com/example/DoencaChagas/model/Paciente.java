package com.example.DoencaChagas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer idPaciente;

    @Column(name = "idade")
    private Integer idade;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "fase_doenca")
    private String faseDoenca;

    @Column(name = "forma_clinica")
    private String formaClinica;

    @Column(name = "via_transmissao")
    private String viaTransmissao;

    @Column(name = "status_paciente")
    private String statusPaciente;

    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @Column(name = "data_atualizacao")
    private LocalDate dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "id_cidade")
    private Cidade cidade;
}
