package com.example.DoencaChagas.controller;

import com.example.DoencaChagas.model.Cidade;
import com.example.DoencaChagas.service.SistemaService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

/**
 * Controller da tela de registro de nova ocorrência (ocorrencia.fxml).
 *
 * Permite que o usuário preencha um formulário com os dados clínicos e
 * epidemiológicos de um paciente com Doença de Chagas e os persista no
 * banco de dados por meio do {@link SistemaService}.
 *
 * Campos do formulário:
 * <ul>
 *   <li>Idade do paciente</li>
 *   <li>Sexo (M / F)</li>
 *   <li>Fase da doença (ex.: Aguda, Crônica)</li>
 *   <li>Forma clínica (ex.: Indeterminada, Cardíaca)</li>
 *   <li>Via de transmissão (ex.: Vetorial, Oral)</li>
 *   <li>Status do paciente (Confirmado, Suspeito, Óbito, Recuperado)</li>
 *   <li>ID da cidade onde o caso foi registrado</li>
 * </ul>
 */
@Component
public class OcorrenciaController {

    // ── Campos do formulário ──────────────────────────────────────────────────

    /** Campo de texto para a idade do paciente (valor numérico inteiro). */
    @FXML
    private TextField txtIdade;

    /** ComboBox de seleção de sexo — opções populadas no {@code initialize()}: "M" ou "F". */
    @FXML
    private ComboBox<String> cbSexo;

    /** Campo de texto livre para a fase da doença (ex.: "Aguda", "Crônica"). */
    @FXML
    private TextField txtFaseDoenca;

    /** Campo de texto livre para a forma clínica (ex.: "Indeterminada", "Cardíaca"). */
    @FXML
    private TextField txtFormaClinica;

    /** Campo de texto livre para a via de transmissão (ex.: "Vetorial", "Oral"). */
    @FXML
    private TextField txtViaTransmissao;

    /**
     * ComboBox de seleção do status do paciente.
     * Opções populadas no {@code initialize()}: Confirmado, Suspeito, Óbito, Recuperado.
     */
    @FXML
    private ComboBox<String> cbStatusPaciente;

    /** Campo de texto para o ID numérico da cidade onde o caso ocorreu. */
    @FXML
    private TextField txtIdCidade;

    /** Label de feedback exibido abaixo do botão (sucesso em verde ou erro em vermelho). */
    @FXML
    private Label lblMensagem;

    // ── Dependência de serviço ────────────────────────────────────────────────

    /** Serviço de negócio que valida e persiste a ocorrência no banco de dados. */
    private final SistemaService sistemaService;

    /**
     * Construtor com injeção de dependências do Spring.
     *
     * @param sistemaService serviço responsável por salvar a ocorrência
     */
    public OcorrenciaController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    /**
     * Chamado automaticamente pelo JavaFX após o carregamento do FXML.
     * Popula as opções fixas dos ComboBoxes (sexo e status do paciente).
     */
    @FXML
    public void initialize() {
        cbSexo.getItems().addAll("M", "F");
        cbStatusPaciente.getItems().addAll("Confirmado", "Suspeito", "Óbito", "Recuperado");
    }

    /**
     * Disparado pelo botão "Salvar Ocorrência" ({@code onAction="#handleSalvar"}).
     *
     * <p>Fluxo de execução:
     * <ol>
     *   <li>Faz o parse de idade e ID da cidade (int). Lança {@code NumberFormatException} se inválido.</li>
     *   <li>Valida que Sexo e Status foram selecionados nos ComboBoxes.</li>
     *   <li>Chama {@link SistemaService#adicionarOcorrencia} para persistir o paciente.</li>
     *   <li>Exibe mensagem de sucesso e limpa o formulário para um novo registro.</li>
     * </ol>
     */
    @FXML
    private void handleSalvar() {
        try {
            // Converte idade e ID da cidade — lança NumberFormatException se o texto não for numérico
            Integer idade = Integer.parseInt(txtIdade.getText());
            String sexo = cbSexo.getValue();
            String faseDoenca = txtFaseDoenca.getText();
            String formaClinica = txtFormaClinica.getText();
            String viaTransmissao = txtViaTransmissao.getText();
            String statusPaciente = cbStatusPaciente.getValue();
            Integer idCidade = Integer.parseInt(txtIdCidade.getText());

            // Campos de ComboBox são obrigatórios — retorna erro se não selecionados
            if (sexo == null || statusPaciente == null) {
                lblMensagem.setText("Por favor, preencha Sexo e Status.");
                lblMensagem.setStyle("-fx-text-fill: #DC3545;");
                return;
            }

            // Persiste a ocorrência no banco via serviço de negócio
            sistemaService.adicionarOcorrencia(idade, sexo, faseDoenca, formaClinica, viaTransmissao, statusPaciente, idCidade);
            
            lblMensagem.setText("Ocorrência registrada com sucesso!");
            lblMensagem.setStyle("-fx-text-fill: #28A745;");
            
            // Limpa o formulário para facilitar o cadastro de múltiplas ocorrências
            limparCampos();

        } catch (NumberFormatException e) {
            lblMensagem.setText("Erro: Idade e ID da Cidade devem ser números válidos.");
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        } catch (Exception e) {
            lblMensagem.setText("Erro ao salvar: " + e.getMessage());
            lblMensagem.setStyle("-fx-text-fill: #DC3545;");
        }
    }

    /**
     * Redefine todos os campos do formulário para o estado vazio/sem seleção,
     * preparando a tela para um novo registro de ocorrência.
     */
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
