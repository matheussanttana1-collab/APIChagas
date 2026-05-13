package com.example.DoencaChagas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Controller principal da aplicação, responsável pela navegação entre as telas.
 *
 * Este controller é vinculado ao arquivo {@code main.fxml}, que define a estrutura
 * geral da janela: uma barra lateral (sidebar) com os botões de menu à esquerda e
 * um painel central ({@code contentArea}) onde cada tela de conteúdo é carregada
 * dinamicamente. Ao clicar em um botão do menu, o método correspondente aqui é
 * chamado e substitui o conteúdo exibido no centro da tela.
 */
@Component
public class MainController {

    /**
     * Painel central onde as sub-telas (FXML) são carregadas e exibidas.
     * Mapeado ao elemento {@code <StackPane fx:id="contentArea">} no main.fxml.
     */
    @FXML
    private StackPane contentArea;

    /**
     * Contexto do Spring, utilizado como factory de controllers para que cada
     * FXML carregado receba seu controller gerenciado pelo Spring (com injeção
     * de dependências funcionando corretamente).
     */
    private final ApplicationContext applicationContext;

    /**
     * Construtor com injeção de dependências do Spring.
     *
     * @param applicationContext contexto da aplicação Spring Boot
     */
    public MainController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Chamado automaticamente pelo JavaFX após o carregamento do main.fxml.
     * Define a tela inicial exibida ao abrir a aplicação (formulário de ocorrência).
     */
    @FXML
    public void initialize() {
        // Carrega a tela de registro de ocorrência como view inicial padrão
        loadView("/fxml/ocorrencia.fxml");
    }

    /**
     * Navega para a tela de registro de nova ocorrência de Doença de Chagas.
     * Disparado pelo botão "➕ Nova Ocorrência" na sidebar.
     */
    @FXML
    private void showAdicionarOcorrencia() {
        loadView("/fxml/ocorrencia.fxml");
    }

    /**
     * Navega para a tela de relatório individual de uma cidade.
     * Disparado pelo botão "📊 Relatório da Cidade" na sidebar.
     */
    @FXML
    private void showRelatorioCidade() {
        loadView("/fxml/relatorio_cidade.fxml");
    }

    /**
     * Navega para a tela de comparação entre duas cidades.
     * Disparado pelo botão "⚖ Comparar Cidades" na sidebar.
     */
    @FXML
    private void showCompararCidades() {
        loadView("/fxml/comparacao.fxml");
    }

    /**
     * Navega para a tela de relatório geral de todas as cidades em um período.
     * Disparado pelo botão "📈 Relatório Geral" na sidebar.
     */
    @FXML
    private void showRelatorioGeral() {
        loadView("/fxml/relatorio_geral.fxml");
    }

    /**
     * Navega para a tela que lista todas as cidades cadastradas no banco de dados.
     * Disparado pelo botão "🏢 Listar Cidades" na sidebar.
     */
    @FXML
    private void showListarCidades() {
        loadView("/fxml/listar_cidades.fxml");
    }

    /**
     * Navega para a tela de geração de relatórios em PDF via JasperReports.
     * Disparado pelo botão "📄 Gerar Relatório PDF" na sidebar.
     */
    @FXML
    private void showGerarPdf() {
        loadView("/fxml/gerar_pdf.fxml");
    }

    /**
     * Carrega um arquivo FXML e substitui o conteúdo atual do {@code contentArea}.
     *
     * Utiliza o {@link ApplicationContext} do Spring como {@code controllerFactory}
     * para que os controllers dos FXMLs filhos sejam beans gerenciados pelo Spring,
     * permitindo injeção de dependências (services, repositórios, etc.).
     *
     * @param fxmlFile caminho do arquivo FXML a ser carregado (relativo ao classpath)
     */
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            // Usa o Spring como fábrica de controllers, garantindo injeção de dependências
            loader.setControllerFactory(applicationContext::getBean);
            Node view = loader.load();
            // Substitui todo o conteúdo do painel central pela nova tela
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
