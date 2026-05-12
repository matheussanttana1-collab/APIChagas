package com.example.DoencaChagas.controller;

import com.example.DoencaChagas.dto.EstatisticasCidadeDTO;
import com.example.DoencaChagas.service.SistemaService;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class ComparacaoController {

    @FXML private TextField txtIdCidadeA;
    @FXML private TextField txtIdCidadeB;
    @FXML private Label lblMensagem;

    @FXML private BarChart<String, Number> barChart;

    @FXML private Label lblNomeA;
    @FXML private Label lblNomeB;
    @FXML private Label lblPopA;
    @FXML private Label lblPopB;
    @FXML private Label lblPercA;
    @FXML private Label lblPercB;
    @FXML private Label lblMortA;
    @FXML private Label lblMortB;

    private final SistemaService sistemaService;

    public ComparacaoController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    @FXML
    private void handleComparar() {
        try {
            Integer idCidadeA = Integer.parseInt(txtIdCidadeA.getText());
            Integer idCidadeB = Integer.parseInt(txtIdCidadeB.getText());

            List<EstatisticasCidadeDTO> comparacao = sistemaService.gerarRelatorioComparacao(idCidadeA, idCidadeB);

            if (comparacao == null || comparacao.size() < 2) {
                lblMensagem.setText("Não foi possível comparar. Cidades não encontradas ou sem dados.");
                lblMensagem.setStyle("-fx-text-fill: #DC3545;");
                return;
            }

            EstatisticasCidadeDTO cidadeA = comparacao.get(0);
            EstatisticasCidadeDTO cidadeB = comparacao.get(1);

            String nomeA = cidadeA.getCidade() != null ? cidadeA.getCidade().getNome() : "Desconhecida";
            String nomeB = cidadeB.getCidade() != null ? cidadeB.getCidade().getNome() : "Desconhecida";

            // Atualiza Labels da tabela comparativa
            lblNomeA.setText(nomeA);
            lblNomeB.setText(nomeB);
            
            lblPopA.setText(cidadeA.getCidade() != null ? String.valueOf(cidadeA.getCidade().getPopulacao()) : "N/A");
            lblPopB.setText(cidadeB.getCidade() != null ? String.valueOf(cidadeB.getCidade().getPopulacao()) : "N/A");
            
            lblPercA.setText(String.format(Locale.US, "%.4f%%", cidadeA.getPercentualCasos()));
            lblPercB.setText(String.format(Locale.US, "%.4f%%", cidadeB.getPercentualCasos()));

            lblMortA.setText(String.format(Locale.US, "%.2f%%", cidadeA.getTaxaMortalidade()));
            lblMortB.setText(String.format(Locale.US, "%.2f%%", cidadeB.getTaxaMortalidade()));

            // Atualiza o Gráfico de Barras
            barChart.getData().clear();

            XYChart.Series<String, Number> seriesA = new XYChart.Series<>();
            seriesA.setName(nomeA);
            seriesA.getData().add(new XYChart.Data<>("Confirmados", cidadeA.getCasosConfirmados()));
            seriesA.getData().add(new XYChart.Data<>("Suspeitos", cidadeA.getSuspeitos()));
            seriesA.getData().add(new XYChart.Data<>("Recuperados", cidadeA.getRecuperados()));
            seriesA.getData().add(new XYChart.Data<>("Óbitos", cidadeA.getObitos()));

            XYChart.Series<String, Number> seriesB = new XYChart.Series<>();
            seriesB.setName(nomeB);
            seriesB.getData().add(new XYChart.Data<>("Confirmados", cidadeB.getCasosConfirmados()));
            seriesB.getData().add(new XYChart.Data<>("Suspeitos", cidadeB.getSuspeitos()));
            seriesB.getData().add(new XYChart.Data<>("Recuperados", cidadeB.getRecuperados()));
            seriesB.getData().add(new XYChart.Data<>("Óbitos", cidadeB.getObitos()));

            barChart.getData().addAll(seriesA, seriesB);

            lblMensagem.setText("Comparação gerada com sucesso!");
            lblMensagem.setStyle("-fx-text-fill: #28A745;");

        } catch (NumberFormatException e) {
            lblMensagem.setText("Erro: Os IDs das cidades devem ser numéricos.");
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        } catch (Exception e) {
            lblMensagem.setText("Erro ao comparar: " + e.getMessage());
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        }
    }
}
