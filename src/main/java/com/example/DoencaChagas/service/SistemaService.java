package com.example.DoencaChagas.service;

import com.example.DoencaChagas.model.Paciente;
import com.example.DoencaChagas.model.Cidade;
import com.example.DoencaChagas.dto.RelatorioCidadeDTO;
import com.example.DoencaChagas.dto.EstatisticasCidadeDTO;
import com.example.DoencaChagas.repository.BancoDeDadosDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Service
public class SistemaService {

    @Autowired
    private BancoDeDadosDAO bancoDeDadosDAO;

    // rtod que adiciona uma nova conciliator da doença
    public Paciente adicionarOcorrencia(Integer idade, String sexo, String faseDoenca,
            String formaClinica, String viaTransmissao,
            String statusPaciente, com.example.DoencaChagas.model.Cidade cidade) {

        if (idade == null) {
            throw new IllegalArgumentException("A idade não pode ser nula.");
        }
        if (sexo == null || sexo.trim().isEmpty()) {
            throw new IllegalArgumentException("O sexo não pode ser nulo ou vazio.");
        }
        if (faseDoenca == null || faseDoenca.trim().isEmpty()) {
            throw new IllegalArgumentException("A fase da doença não pode ser nula ou vazia.");
        }
        if (formaClinica == null || formaClinica.trim().isEmpty()) {
            throw new IllegalArgumentException("A forma clínica não pode ser nula ou vazia.");
        }
        if (viaTransmissao == null || viaTransmissao.trim().isEmpty()) {
            throw new IllegalArgumentException("A via de transmissão não pode ser nula ou vazia.");
        }
        if (statusPaciente == null || statusPaciente.trim().isEmpty()) {
            throw new IllegalArgumentException("O status do paciente não pode ser nulo ou vazio.");
        }
        if (cidade == null) {
            throw new IllegalArgumentException("A cidade não pode ser nula.");
        }

        Paciente novoPaciente = Paciente.builder()
                .idade(idade)
                .sexo(sexo)
                .faseDoenca(faseDoenca)
                .formaClinica(formaClinica)
                .viaTransmissao(viaTransmissao)
                .statusPaciente(statusPaciente)
                .dataRegistro(java.time.LocalDate.now())
                .dataAtualizacao(java.time.LocalDate.now())
                .cidade(cidade)
                .build();

        return bancoDeDadosDAO.adicionarNovaOcorrencia(novoPaciente);
    }

    public RelatorioCidadeDTO gerarRelatorioCidade(Integer idCidade, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            quantidade = 20;
        }

        Cidade cidade = bancoDeDadosDAO.findCidadeById(idCidade);
        if (cidade == null) {
            throw new RuntimeException("Cidade não encontrada com o ID: " + idCidade);
        }

        List<Paciente> pacientes = bancoDeDadosDAO.findByCidade_IdCidade(idCidade, PageRequest.of(0, quantidade));

        if (pacientes == null || pacientes.isEmpty()) {
            throw new RuntimeException("Não existem pacientes cadastrados para esta cidade.");
        }

        long totalCasos = pacientes.size();
        EstatisticasCidadeDTO estatisticas = calcularEstatisticas(cidade, pacientes, totalCasos);

        return RelatorioCidadeDTO.builder()
                .cidade(cidade)
                .pacientes(pacientes)
                .percentualCasos(estatisticas.getPercentualCasos())
                .casosConfirmados(estatisticas.getCasosConfirmados())
                .obitos(estatisticas.getObitos())
                .recuperados(estatisticas.getRecuperados())
                .suspeitos(estatisticas.getSuspeitos())
                .taxaMortalidade(estatisticas.getTaxaMortalidade())
                .build();
    }

    private EstatisticasCidadeDTO calcularEstatisticas(Cidade cidade, List<Paciente> pacientes, long totalCasos) {
        double percentual = 0.0;
        if (cidade.getPopulacao() != null && cidade.getPopulacao() > 0) {
            percentual = (double) totalCasos / cidade.getPopulacao() * 100.0;
        }

        long casosConfirmados = pacientes.stream()
                .filter(p -> p.getStatusPaciente() != null && p.getStatusPaciente().equalsIgnoreCase("Confirmado"))
                .count();

        long obitos = pacientes.stream()
                .filter(p -> p.getStatusPaciente() != null && p.getStatusPaciente().equalsIgnoreCase("Óbito"))
                .count();

        long recuperados = pacientes.stream()
                .filter(p -> p.getStatusPaciente() != null && p.getStatusPaciente().equalsIgnoreCase("Recuperado"))
                .count();

        long suspeitos = pacientes.stream()
                .filter(p -> p.getStatusPaciente() != null && p.getStatusPaciente().equalsIgnoreCase("Suspeito"))
                .count();

        double taxaMortalidade = 0.0;
        if (casosConfirmados > 0) {
            taxaMortalidade = ((double) obitos / casosConfirmados) * 100.0;
        }

        return EstatisticasCidadeDTO.builder()
                .cidade(cidade)
                .percentualCasos(percentual)
                .casosConfirmados(casosConfirmados)
                .obitos(obitos)
                .recuperados(recuperados)
                .suspeitos(suspeitos)
                .taxaMortalidade(taxaMortalidade)
                .build();
    }

    // rtod que gera relatório de comparator entre duas cidades
    public List<EstatisticasCidadeDTO> gerarRelatorioComparacao(Integer idCidade1, Integer idCidade2) {
        List<EstatisticasCidadeDTO> comparacao = new java.util.ArrayList<>();

        // Process Cidade 1
        Cidade cidade1 = bancoDeDadosDAO.findCidadeById(idCidade1);
        if (cidade1 != null) {
            List<Paciente> pacientes1 = bancoDeDadosDAO.findByCidade_IdCidade(idCidade1);
            if (pacientes1 != null && !pacientes1.isEmpty()) {
                long totalCasos1 = pacientes1.size();
                comparacao.add(calcularEstatisticas(cidade1, pacientes1, totalCasos1));
            }
        }

        // Process Cidade 2
        Cidade cidade2 = bancoDeDadosDAO.findCidadeById(idCidade2);
        if (cidade2 != null) {
            List<Paciente> pacientes2 = bancoDeDadosDAO.findByCidade_IdCidade(idCidade2);
            if (pacientes2 != null && !pacientes2.isEmpty()) {
                long totalCasos2 = pacientes2.size();
                comparacao.add(calcularEstatisticas(cidade2, pacientes2, totalCasos2));
            }
        }

        return comparacao;
    }

    // rtod que gera relatório de todas as cidades num intervalo de tempo
    public List<EstatisticasCidadeDTO> gerarRelatorioTodasCidades(java.time.LocalDate dataInicial, java.time.LocalDate dataFinal) {
        List<EstatisticasCidadeDTO> relatorioGeral = new java.util.ArrayList<>();

        // Buscar todas as cidades
        List<Cidade> todasCidades = bancoDeDadosDAO.findAllCidades();

        if (todasCidades != null) {
            for (Cidade cidade : todasCidades) {
                // Para cada cidade, buscar pacientes no intervalo
                List<Paciente> pacientes = bancoDeDadosDAO.findByCidade_IdCidadeAndDataRegistroBetween(cidade.getIdCidade(), dataInicial, dataFinal);
                
                if (pacientes != null && !pacientes.isEmpty()) {
                    long totalCasosNoPeriodo = pacientes.size();
                    relatorioGeral.add(calcularEstatisticas(cidade, pacientes, totalCasosNoPeriodo));
                } else {
                    // Se não tiver paciente no período, cria um DTO zerado
                    relatorioGeral.add(EstatisticasCidadeDTO.builder()
                            .cidade(cidade)
                            .percentualCasos(0.0)
                            .casosConfirmados(0)
                            .obitos(0)
                            .recuperados(0)
                            .suspeitos(0)
                            .taxaMortalidade(0.0)
                            .build());
                }
            }
        }

        return relatorioGeral;
    }
}
