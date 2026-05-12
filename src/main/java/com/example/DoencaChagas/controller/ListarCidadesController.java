package com.example.DoencaChagas.controller;

import com.example.DoencaChagas.model.Cidade;
import com.example.DoencaChagas.repository.BancoDeDadosDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListarCidadesController {

    @FXML private Label lblMensagem;

    @FXML private TableView<Cidade> tableCidades;
    @FXML private TableColumn<Cidade, Integer> colId;
    @FXML private TableColumn<Cidade, String> colNome;
    @FXML private TableColumn<Cidade, Integer> colPopulacao;

    private final BancoDeDadosDAO bancoDeDadosDAO;

    public ListarCidadesController(BancoDeDadosDAO bancoDeDadosDAO) {
        this.bancoDeDadosDAO = bancoDeDadosDAO;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCidade"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPopulacao.setCellValueFactory(new PropertyValueFactory<>("populacao"));
        
        carregarCidades();
    }

    @FXML
    private void handleAtualizar() {
        carregarCidades();
    }

    private void carregarCidades() {
        try {
            List<Cidade> cidades = bancoDeDadosDAO.findAllCidades();
            if (cidades == null || cidades.isEmpty()) {
                lblMensagem.setText("Nenhuma cidade encontrada no banco de dados.");
                lblMensagem.setStyle("-fx-text-fill: #333333;");
                tableCidades.getItems().clear();
            } else {
                ObservableList<Cidade> list = FXCollections.observableArrayList(cidades);
                tableCidades.setItems(list);
                lblMensagem.setText(cidades.size() + " cidades carregadas.");
                lblMensagem.setStyle("-fx-text-fill: #28A745;");
            }
        } catch (Exception e) {
            lblMensagem.setText("Erro ao buscar cidades: " + e.getMessage());
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        }
    }
}
