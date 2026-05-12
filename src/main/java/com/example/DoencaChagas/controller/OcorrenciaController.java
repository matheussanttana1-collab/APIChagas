package com.example.DoencaChagas.controller;

import com.example.DoencaChagas.model.Cidade;
import com.example.DoencaChagas.service.SistemaService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class OcorrenciaController {

    @FXML
    private TextField txtIdade;
    @FXML
    private ComboBox<String> cbSexo;
    @FXML
    private TextField txtFaseDoenca;
    @FXML
    private TextField txtFormaClinica;
    @FXML
    private TextField txtViaTransmissao;
    @FXML
    private ComboBox<String> cbStatusPaciente;
    @FXML
    private TextField txtIdCidade;
    @FXML
    private Label lblMensagem;

    private final SistemaService sistemaService;

    public OcorrenciaController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    @FXML
    public void initialize() {
        cbSexo.getItems().addAll("M", "F");
        cbStatusPaciente.getItems().addAll("Confirmado", "Suspeito", "Óbito", "Recuperado");
    }

    @FXML
    private void handleSalvar() {
        try {
            Integer idade = Integer.parseInt(txtIdade.getText());
            String sexo = cbSexo.getValue();
            String faseDoenca = txtFaseDoenca.getText();
            String formaClinica = txtFormaClinica.getText();
            String viaTransmissao = txtViaTransmissao.getText();
            String statusPaciente = cbStatusPaciente.getValue();
            Integer idCidade = Integer.parseInt(txtIdCidade.getText());

            if (sexo == null || statusPaciente == null) {
                lblMensagem.setText("Por favor, preencha Sexo e Status.");
                lblMensagem.setStyle("-fx-text-fill: #DC3545;");
                return;
            }

            sistemaService.adicionarOcorrencia(idade, sexo, faseDoenca, formaClinica, viaTransmissao, statusPaciente, idCidade);
            
            lblMensagem.setText("Ocorrência registrada com sucesso!");
            lblMensagem.setStyle("-fx-text-fill: #28A745;");
            
            limparCampos();

        } catch (NumberFormatException e) {
            lblMensagem.setText("Erro: Idade e ID da Cidade devem ser números válidos.");
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        } catch (Exception e) {
            lblMensagem.setText("Erro ao salvar: " + e.getMessage());
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        }
    }

    private void limparCampos() {
        txtIdade.clear();
        cbSexo.getSelectionModel().clearSelection();
        txtFaseDoenca.clear();
        txtFormaClinica.clear();
        txtViaTransmissao.clear();
        cbStatusPaciente.getSelectionModel().clearSelection();
        txtIdCidade.clear();
    }
}
