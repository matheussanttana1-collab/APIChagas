package com.example.DoencaChagas.controller;

import com.example.DoencaChagas.service.RelatorioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class GerarPdfController {

    // ── FXML bindings ──────────────────────────────────────────────
    @FXML private ComboBox<String> cbTipoRelatorio;
    @FXML private Button btnGerarPdf;
    @FXML private Label lblMensagem;

    // Painel: ID único de cidade (relatório individual)
    @FXML private HBox hboxIdCidade;
    @FXML private TextField txtIdCidade;

    // Painel: dois IDs de cidade (relatórios comparativos)
    @FXML private HBox hboxDuasCidades;
    @FXML private TextField txtCidade1Id;
    @FXML private TextField txtCidade2Id;

    // ── Constantes de exibição → nome do arquivo JRXML ─────────────
    private static final String REL_INDIVIDUAL   = "Relatório Individual de Cidade";
    private static final String REL_GERAL        = "Relatório Geral (todos os pacientes)";
    private static final String REL_COMP_CIDADE  = "Comparativo por Faixa Etária (2 cidades)";
    private static final String REL_COMP_DATA    = "Comparativo por Data de Coleta (2 cidades)";

    private static final String JRXML_INDIVIDUAL  = "relatorio_individual_cidade";
    private static final String JRXML_GERAL       = "relatorio_geral";
    private static final String JRXML_COMP_CIDADE = "relatorio_comparativo_cidade";
    private static final String JRXML_COMP_DATA   = "relatorio_comparativo_data";

    // ── Dependência ────────────────────────────────────────────────
    private final RelatorioService relatorioService;

    public GerarPdfController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    // ── Inicialização ──────────────────────────────────────────────
    @FXML
    public void initialize() {
        cbTipoRelatorio.setItems(FXCollections.observableArrayList(
                REL_INDIVIDUAL,
                REL_GERAL,
                REL_COMP_CIDADE,
                REL_COMP_DATA
        ));
    }

    // ── Evento: tipo de relatório alterado ─────────────────────────
    @FXML
    private void handleTipoRelatorioChanged() {
        String sel = cbTipoRelatorio.getValue();
        if (sel == null) return;

        // Oculta todos os painéis de parâmetro
        mostrarPainelIdCidade(false);
        mostrarPainelDuasCidades(false);
        lblMensagem.setText("");

        switch (sel) {
            case REL_INDIVIDUAL  -> mostrarPainelIdCidade(true);
            case REL_GERAL       -> { /* sem parâmetros extras */ }
            case REL_COMP_CIDADE -> mostrarPainelDuasCidades(true);
            case REL_COMP_DATA   -> mostrarPainelDuasCidades(true);
        }

        btnGerarPdf.setDisable(false);
    }

    // ── Evento: clique em "Gerar PDF" ──────────────────────────────
    @FXML
    private void handleGerarPdf() {
        String tipo = cbTipoRelatorio.getValue();
        if (tipo == null) {
            mostrarErro("Selecione um tipo de relatório.");
            return;
        }

        try {
            byte[] pdfBytes;
            String nomeArquivo;

            switch (tipo) {

                case REL_INDIVIDUAL -> {
                    int idCidade = parseCampoInt(txtIdCidade, "ID da Cidade");
                    Map<String, Object> params = new HashMap<>();
                    params.put("ID_CIDADE", idCidade);
                    pdfBytes    = relatorioService.gerarPDF(JRXML_INDIVIDUAL, params);
                    nomeArquivo = "relatorio_individual_cidade_" + idCidade + ".pdf";
                }

                case REL_GERAL -> {
                    // Relatório geral não exige parâmetros do usuário
                    pdfBytes    = relatorioService.gerarPDF(JRXML_GERAL, null);
                    nomeArquivo = "relatorio_geral.pdf";
                }

                case REL_COMP_CIDADE -> {
                    int c1 = parseCampoInt(txtCidade1Id, "ID Cidade 1");
                    int c2 = parseCampoInt(txtCidade2Id, "ID Cidade 2");
                    Map<String, Object> params = new HashMap<>();
                    params.put("CIDADE1_ID", c1);
                    params.put("CIDADE2_ID", c2);
                    pdfBytes    = relatorioService.gerarPDF(JRXML_COMP_CIDADE, params);
                    nomeArquivo = "relatorio_comparativo_cidade_" + c1 + "_vs_" + c2 + ".pdf";
                }

                case REL_COMP_DATA -> {
                    int c1 = parseCampoInt(txtCidade1Id, "ID Cidade 1");
                    int c2 = parseCampoInt(txtCidade2Id, "ID Cidade 2");
                    Map<String, Object> params = new HashMap<>();
                    params.put("CIDADE1_ID", c1);
                    params.put("CIDADE2_ID", c2);
                    pdfBytes    = relatorioService.gerarPDF(JRXML_COMP_DATA, params);
                    nomeArquivo = "relatorio_comparativo_data_" + c1 + "_vs_" + c2 + ".pdf";
                }

                default -> {
                    mostrarErro("Tipo de relatório desconhecido.");
                    return;
                }
            }

            salvarEAbrirPdf(pdfBytes, nomeArquivo);

        } catch (NumberFormatException e) {
            mostrarErro("Erro de formato: " + e.getMessage());
        } catch (Exception e) {
            mostrarErro("Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ── Helpers ────────────────────────────────────────────────────

    private void salvarEAbrirPdf(byte[] pdfBytes, String nomeArquivo) throws Exception {
        File dir = new File("relatorios");
        if (!dir.exists()) dir.mkdirs();

        File arquivoPdf = new File(dir, nomeArquivo);
        try (FileOutputStream fos = new FileOutputStream(arquivoPdf)) {
            fos.write(pdfBytes);
        }

        mostrarSucesso("PDF gerado: " + arquivoPdf.getAbsolutePath());

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            Desktop.getDesktop().open(arquivoPdf);
        }
    }

    private int parseCampoInt(TextField campo, String nomeCampo) {
        String texto = campo.getText();
        if (texto == null || texto.isBlank()) {
            throw new NumberFormatException("O campo \"" + nomeCampo + "\" está vazio.");
        }
        return Integer.parseInt(texto.trim());
    }

    private void mostrarPainelIdCidade(boolean visivel) {
        hboxIdCidade.setVisible(visivel);
        hboxIdCidade.setManaged(visivel);
    }

    private void mostrarPainelDuasCidades(boolean visivel) {
        hboxDuasCidades.setVisible(visivel);
        hboxDuasCidades.setManaged(visivel);
    }

    private void mostrarErro(String msg) {
        lblMensagem.setText("❌ " + msg);
        lblMensagem.setStyle("-fx-text-fill: #DC3545;");
    }

    private void mostrarSucesso(String msg) {
        lblMensagem.setText("✅ " + msg);
        lblMensagem.setStyle("-fx-text-fill: #28A745;");
    }
}
