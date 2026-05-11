package com.example.DoencaChagas.dto;

import com.example.DoencaChagas.model.Paciente;
import com.example.DoencaChagas.model.Cidade;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioCidadeDTO {
    private Cidade cidade;
    private List<Paciente> pacientes;
    private double percentualCasos;
    private long casosConfirmados;
    private long obitos;
    private long recuperados;
    private long suspeitos;
    private double taxaMortalidade;
}
