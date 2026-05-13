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

/**
 * Controller da tela de comparação entre duas cidades (comparacao.fxml).
 *
 * Permite que o usuário informe os IDs de duas cidades cadastradas e gere
 * uma comparação lado a lado com os seguintes dados epidemiológicos:
 * população total, percentual de casos confirmados e taxa de mortalidade.
 * Os dados são exibidos em uma tabela resumida e em um gráfico de barras
 * com as categorias: Confirmados, Suspeitos, Recuperados e Óbitos.
 */
@Component
public class ComparacaoController {

    // ── Campos de entrada ─────────────────────────────────────────────────────

    /** Campo de texto para o ID numérico da primeira cidade (Cidade A). */
    @FXML private TextField txtIdCidadeA;

    /** Campo de texto para o ID numérico da segunda cidade (Cidade B). */
    @FXML private TextField txtIdCidadeB;

    /** Label de feedback exibido após a operação (sucesso ou erro). */
    @FXML private Label lblMensagem;

    // ── Gráfico de barras ─────────────────────────────────────────────────────

    /**
     * Gráfico de barras agrupado que exibe, lado a lado para cada cidade,
     * o número de casos confirmados, suspeitos, recuperados e óbitos.
     */
    @FXML private BarChart<String, Number> barChart;

    // ── Labels da tabela comparativa ──────────────────────────────────────────

    /** Nome da Cidade A (preenchido após a busca). */
    @FXML private Label lblNomeA;

    /** Nome da Cidade B (preenchido após a busca). */
    @FXML private Label lblNomeB;

    /** População total da Cidade A. */
    @FXML private Label lblPopA;

    /** População total da Cidade B. */
    @FXML private Label lblPopB;

    /** Percentual de casos confirmados em relação à população da Cidade A. */
    @FXML private Label lblPercA;

    /** Percentual de casos confirmados em relação à população da Cidade B. */
    @FXML private Label lblPercB;

    /** Taxa de mortalidade (óbitos / confirmados) da Cidade A. */
    @FXML private Label lblMortA;

    /** Taxa de mortalidade (óbitos / confirmados) da Cidade B. */
    @FXML private Label lblMortB;

    // ── Dependência de serviço ────────────────────────────────────────────────

    /** Serviço de negócio responsável pelas consultas e cálculos estatísticos. */
    private final SistemaService sistemaService;

    /**
     * Construtor com injeção de dependências do Spring.
     *
     * @param sistemaService serviço que executa a comparação entre cidades
     */
    public ComparacaoController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    /**
     * Disparado pelo botão "Comparar" no FXML ({@code onAction="#handleComparar"}).
     *
     * <p>Fluxo de execução:
     * <ol>
     *   <li>Faz o parse dos IDs digitados pelo usuário.</li>
     *   <li>Chama {@link SistemaService#gerarRelatorioComparacao} para obter as estatísticas.</li>
     *   <li>Preenche os labels da tabela comparativa com nome, população, % de casos e mortalidade.</li>
     *   <li>Monta duas séries no {@code barChart}: uma por cidade, com 4 categorias cada.</li>
     * </ol>
     * Em caso de ID não numérico ou cidades não encontradas, exibe mensagem de erro no {@code lblMensagem}.
     */
    @FXML
    private void handleComparar() {
        try {
            // Converte os textos digitados para inteiros (lança NumberFormatException se inválido)
            Integer idCidadeA = Integer.parseInt(txtIdCidadeA.getText());
            Integer idCidadeB = Integer.parseInt(txtIdCidadeB.getText());

            // Consulta o serviço para obter as estatísticas das duas cidades
            List<EstatisticasCidadeDTO> comparacao = sistemaService.gerarRelatorioComparacao(idCidadeA, idCidadeB);

            // Valida se o resultado retornou dados para ambas as cidades
            if (comparacao == null || comparacao.size() < 2) {
                lblMensagem.setText("Não foi possível comparar. Cidades não encontradas ou sem dados.");
                lblMensagem.setStyle("-fx-text-fill: #DC3545;");
                return;
            }

            // Extrai os DTOs para cada cidade
            EstatisticasCidadeDTO cidadeA = comparacao.get(0);
            EstatisticasCidadeDTO cidadeB = comparacao.get(1);

            // Obtém os nomes com fallback para "Desconhecida" caso o dado seja nulo
            String nomeA = cidadeA.getCidade() != null ? cidadeA.getCidade().getNome() : "Desconhecida";
            String nomeB = cidadeB.getCidade() != null ? cidadeB.getCidade().getNome() : "Desconhecida";

            // Atualiza Labels da tabela comparativa
            lblNomeA.setText(nomeA);
            lblNomeB.setText(nomeB);
            
            lblPopA.setText(cidadeA.getCidade() != null ? String.valueOf(cidadeA.getCidade().getPopulacao()) : "N/A");
            lblPopB.setText(cidadeB.getCidade() != null ? String.valueOf(cidadeB.getCidade().getPopulacao()) : "N/A");
            
            // Formata os percentuais com 4 casas decimais usando ponto como separador decimal
            lblPercA.setText(String.format(Locale.US, "%.4f%%", cidadeA.getPercentualCasos()));
            lblPercB.setText(String.format(Locale.US, "%.4f%%", cidadeB.getPercentualCasos()));

            lblMortA.setText(String.format(Locale.US, "%.2f%%", cidadeA.getTaxaMortalidade()));
            lblMortB.setText(String.format(Locale.US, "%.2f%%", cidadeB.getTaxaMortalidade()));

            // Atualiza o Gráfico de Barras — limpa dados anteriores antes de adicionar os novos
            barChart.getData().clear();

            // Série de dados da Cidade A com 4 categorias de status
            XYChart.Series<String, Number> seriesA = new XYChart.Series<>();
            seriesA.setName(nomeA);
            seriesA.getData().add(new XYChart.Data<>("Confirmados", cidadeA.getCasosConfirmados()));
            seriesA.getData().add(new XYChart.Data<>("Suspeitos", cidadeA.getSuspeitos()));
            seriesA.getData().add(new XYChart.Data<>("Recuperados", cidadeA.getRecuperados()));
            seriesA.getData().add(new XYChart.Data<>("Óbitos", cidadeA.getObitos()));

            // Série de dados da Cidade B com as mesmas categorias
            XYChart.Series<String, Number> seriesB = new XYChart.Series<>();
            seriesB.setName(nomeB);
            seriesB.getData().add(new XYChart.Data<>("Confirmados", cidadeB.getCasosConfirmados()));
            seriesB.getData().add(new XYChart.Data<>("Suspeitos", cidadeB.getSuspeitos()));
            seriesB.getData().add(new XYChart.Data<>("Recuperados", cidadeB.getRecuperados()));
            seriesB.getData().add(new XYChart.Data<>("Óbitos", cidadeB.getObitos()));

            // Adiciona ambas as séries ao gráfico para renderização lado a lado
            barChart.getData().addAll(seriesA, seriesB);

            lblMensagem.setText("Comparação gerada com sucesso!");
            lblMensagem.setStyle("-fx-text-fill: #28A745;");

        } catch (NumberFormatException e) {
            // IDs não numéricos digitados pelo usuário
            lblMensagem.setText("Erro: Os IDs das cidades devem ser numéricos.");
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        } catch (Exception e) {
            // Erros de banco de dados ou de lógica de negócio
            lblMensagem.setText("Erro ao comparar: " + e.getMessage());
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        }
    }
}
