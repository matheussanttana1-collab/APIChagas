package com.example.DoencaChagas.dto;

import com.example.DoencaChagas.model.Cidade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticasCidadeDTO {
    private Cidade cidade;
    private double percentualCasos;
    private long casosConfirmados;
    private long obitos;
    private long recuperados;
    private long suspeitos;
    private double taxaMortalidade;
}
