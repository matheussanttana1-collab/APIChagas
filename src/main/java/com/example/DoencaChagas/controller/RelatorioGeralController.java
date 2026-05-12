package com.example.DoencaChagas.controller;

import com.example.DoencaChagas.dto.EstatisticasCidadeDTO;
import com.example.DoencaChagas.service.SistemaService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RelatorioGeralController {

    @FXML private DatePicker dpDataInicial;
    @FXML private DatePicker dpDataFinal;
    @FXML private Label lblMensagem;

    @FXML private TableView<EstatisticasCidadeDTO> tableRelatorio;
    @FXML private TableColumn<EstatisticasCidadeDTO, String> colCidade;
    @FXML private TableColumn<EstatisticasCidadeDTO, Integer> colPopulacao;
    @FXML private TableColumn<EstatisticasCidadeDTO, Long> colConfirmados;
    @FXML private TableColumn<EstatisticasCidadeDTO, Long> colSuspeitos;
    @FXML private TableColumn<EstatisticasCidadeDTO, Long> colRecuperados;
    @FXML private TableColumn<EstatisticasCidadeDTO, Long> colObitos;
    @FXML private TableColumn<EstatisticasCidadeDTO, String> colMortalidade;

    private final SistemaService sistemaService;

    public RelatorioGeralController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    @FXML
    public void initialize() {
        colCidade.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCidade() != null) {
                return new SimpleStringProperty(cellData.getValue().getCidade().getNome());
            }
            return new SimpleStringProperty("N/A");
        });
        
        colPopulacao.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCidade() != null) {
                return new SimpleIntegerProperty(cellData.getValue().getCidade().getPopulacao()).asObject();
            }
            return new SimpleIntegerProperty(0).asObject();
        });

        colConfirmados.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getCasosConfirmados()).asObject());
        colSuspeitos.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getSuspeitos()).asObject());
        colRecuperados.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getRecuperados()).asObject());
        colObitos.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getObitos()).asObject());
        
        colMortalidade.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.format(java.util.Locale.US, "%.2f%%", cellData.getValue().getTaxaMortalidade()))
        );
    }

    @FXML
    private void handleBuscar() {
        LocalDate dataInicial = dpDataInicial.getValue();
        LocalDate dataFinal = dpDataFinal.getValue();

        if (dataInicial == null || dataFinal == null) {
            lblMensagem.setText("Por favor, selecione ambas as datas.");
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
            return;
        }

        try {
            List<EstatisticasCidadeDTO> relatorio = sistemaService.gerarRelatorioTodasCidades(dataInicial, dataFinal);

            if (relatorio == null || relatorio.isEmpty()) {
                lblMensagem.setText("Nenhum dado encontrado no período especificado.");
                lblMensagem.setStyle("-fx-text-fill: #333333;");
                tableRelatorio.getItems().clear();
                return;
            }

            ObservableList<EstatisticasCidadeDTO> dataList = FXCollections.observableArrayList(relatorio);
            tableRelatorio.setItems(dataList);

            lblMensagem.setText("Relatório gerado com sucesso (" + relatorio.size() + " cidades).");
            lblMensagem.setStyle("-fx-text-fill: #28A745;");

        } catch (Exception e) {
            lblMensagem.setText("Erro ao gerar relatório: " + e.getMessage());
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        }
    }
}
