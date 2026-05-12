package com.example.DoencaChagas.controller;

import com.example.DoencaChagas.dto.RelatorioCidadeDTO;
import com.example.DoencaChagas.model.Paciente;
import com.example.DoencaChagas.service.SistemaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class RelatorioCidadeController {

    @FXML private TextField txtIdCidade;
    @FXML private TextField txtQuantidade;
    @FXML private Label lblMensagem;

    @FXML private Label lblNomeCidade;
    @FXML private Label lblPopulacao;
    @FXML private Label lblPercentual;
    @FXML private Label lblTaxaMortalidade;

    @FXML private PieChart pieChartCasos;

    @FXML private TableView<Paciente> tablePacientes;
    @FXML private TableColumn<Paciente, Integer> colId;
    @FXML private TableColumn<Paciente, Integer> colIdade;
    @FXML private TableColumn<Paciente, String> colSexo;
    @FXML private TableColumn<Paciente, String> colStatus;
    @FXML private TableColumn<Paciente, String> colFase;

    private final SistemaService sistemaService;

    public RelatorioCidadeController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPaciente"));
        colIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusPaciente"));
        colFase.setCellValueFactory(new PropertyValueFactory<>("faseDoenca"));
    }

    @FXML
    private void handleBuscar() {
        try {
            Integer idCidade = Integer.parseInt(txtIdCidade.getText());
            Integer quantidade = Integer.parseInt(txtQuantidade.getText());

            RelatorioCidadeDTO relatorio = sistemaService.gerarRelatorioCidade(idCidade, quantidade);

            if (relatorio.getCidade() != null && relatorio.getCidade().getNome() != null) {
                lblNomeCidade.setText(relatorio.getCidade().getNome());
                lblPopulacao.setText(String.valueOf(relatorio.getCidade().getPopulacao()));
            } else {
                lblNomeCidade.setText("Desconhecida");
                lblPopulacao.setText("N/A");
            }

            lblPercentual.setText(String.format(Locale.US, "%.4f%%", relatorio.getPercentualCasos()));
            lblTaxaMortalidade.setText(String.format(Locale.US, "%.2f%%", relatorio.getTaxaMortalidade()));

            // Atualizar Gráfico de Pizza
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Confirmados (" + relatorio.getCasosConfirmados() + ")", relatorio.getCasosConfirmados()),
                new PieChart.Data("Suspeitos (" + relatorio.getSuspeitos() + ")", relatorio.getSuspeitos()),
                new PieChart.Data("Recuperados (" + relatorio.getRecuperados() + ")", relatorio.getRecuperados()),
                new PieChart.Data("Óbitos (" + relatorio.getObitos() + ")", relatorio.getObitos())
            );
            pieChartCasos.setData(pieChartData);

            // Atualizar Tabela
            ObservableList<Paciente> pacientesList = FXCollections.observableArrayList(relatorio.getPacientes());
            tablePacientes.setItems(pacientesList);

            lblMensagem.setText("Dados carregados com sucesso!");
            lblMensagem.setStyle("-fx-text-fill: #28A745;");

        } catch (NumberFormatException e) {
            lblMensagem.setText("Erro: Os campos ID e Quantidade devem ser números.");
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        } catch (Exception e) {
            lblMensagem.setText("Erro ao buscar relatório: " + e.getMessage());
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        }
    }
}
