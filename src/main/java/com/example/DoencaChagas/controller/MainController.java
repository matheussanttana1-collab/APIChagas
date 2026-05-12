package com.example.DoencaChagas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainController {

    @FXML
    private StackPane contentArea;

    private final ApplicationContext applicationContext;

    public MainController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @FXML
    public void initialize() {
        // Por padrão, pode carregar uma tela de boas vindas ou a lista de cidades
        loadView("/fxml/ocorrencia.fxml");
    }

    @FXML
    private void showAdicionarOcorrencia() {
        loadView("/fxml/ocorrencia.fxml");
    }

    @FXML
    private void showRelatorioCidade() {
        loadView("/fxml/relatorio_cidade.fxml");
    }

    @FXML
    private void showCompararCidades() {
        loadView("/fxml/comparacao.fxml");
    }

    @FXML
    private void showRelatorioGeral() {
        loadView("/fxml/relatorio_geral.fxml");
    }

    @FXML
    private void showListarCidades() {
        loadView("/fxml/listar_cidades.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setControllerFactory(applicationContext::getBean);
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
