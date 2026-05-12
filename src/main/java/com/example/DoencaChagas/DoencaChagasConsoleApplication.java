package com.example.DoencaChagas;

import com.example.DoencaChagas.model.Cidade;
import com.example.DoencaChagas.model.Paciente;
import com.example.DoencaChagas.service.SistemaService;
import com.example.DoencaChagas.repository.BancoDeDadosDAO;
import org.springframework.boot.CommandLineRunner;

import java.util.Scanner;

// Esta classe foi mantida como backup da implementação via console.
// A anotação @SpringBootApplication e @Bean foram removidas para não conflitar com a nova inicialização do JavaFX.
public class DoencaChagasConsoleApplication {

	public CommandLineRunner runConsole(SistemaService sistemaService, BancoDeDadosDAO bancoDeDadosDAO) {
		return args -> {
			Scanner scanner = new Scanner(System.in);
			boolean running = true;

			while (running) {
				System.out.println("\n=======================================================");
				System.out.println("||      SISTEMA DE MONITORAMENTO - DOENÇA DE CHAGAS  ||");
				System.out.println("=======================================================");
				System.out.println("|| 1. Adicionar Nova Ocorrência                      ||");
				System.out.println("|| 2. Gerar Relatório da Cidade                      ||");
				System.out.println("|| 3. Comparar Duas Cidades                          ||");
				System.out.println("|| 4. Relatório Geral (Todas as Cidades)             ||");
				System.out.println("|| 5. Listar Cidades Cadastradas                     ||");
				System.out.println("|| 0. Sair do Sistema                                ||");
				System.out.println("=======================================================");
				System.out.print("Escolha uma opção: ");
				
				int opcao = 0;
				try {
					opcao = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					opcao = -1;
				}

				switch (opcao) {
					case 1:
						adicionarOcorrencia(scanner, sistemaService);
						break;
					case 2:
						gerarRelatorio(scanner, sistemaService);
						break;
					case 3:
						compararCidades(scanner, sistemaService);
						break;
					case 4:
						relatorioGeral(scanner, sistemaService);
						break;
					case 5:
						listarCidades(bancoDeDadosDAO);
						break;
					case 0:
						running = false;
						System.out.println("\nEncerrando o sistema... Até logo!");
						break;
					default:
						System.out.println("\n[!] Opção inválida. Tente novamente.");
				}
			}
		};
	}

	private void adicionarOcorrencia(Scanner scanner, SistemaService sistemaService) {
		System.out.println("\n--- [ NOVA OCORRÊNCIA ] ---");
		try {
			System.out.print("Idade: ");
			Integer idade = Integer.parseInt(scanner.nextLine());

			System.out.print("Sexo (M/F): ");
			String sexo = scanner.nextLine();

			System.out.print("Fase da Doença (Ex: Aguda, Crônica): ");
			String faseDoenca = scanner.nextLine();

			System.out.print("Forma Clínica (Ex: Indeterminada, Cardíaca): ");
			String formaClinica = scanner.nextLine();

			System.out.print("Via de Transmissão (Ex: Vetorial, Oral): ");
			String viaTransmissao = scanner.nextLine();

			System.out.print("Status do Paciente (Confirmado / Suspeito / Óbito / Recuperado): ");
			String statusPaciente = scanner.nextLine();

			System.out.print("ID da Cidade: ");
			Integer idCidade = Integer.parseInt(scanner.nextLine());

			Cidade cidade = new Cidade();
			cidade.setIdCidade(idCidade);

			Paciente p = sistemaService.adicionarOcorrencia(idade, sexo, faseDoenca, formaClinica, viaTransmissao, statusPaciente, idCidade);
			
			System.out.println("\n[+] Sucesso! Ocorrência registrada.");
			System.out.println("ID do Paciente gerado: " + p.getIdPaciente());

		} catch (Exception e) {
			System.out.println("\n[X] Erro ao registrar ocorrência: " + e.getMessage());
		}
	}

	private void gerarRelatorio(Scanner scanner, SistemaService sistemaService) {
		System.out.println("\n--- [ GERAR RELATÓRIO DA CIDADE ] ---");
		try {
			System.out.print("Digite o ID da cidade: ");
			Integer idCidade = Integer.parseInt(scanner.nextLine());

			System.out.print("Quantidade máxima de registros a listar: ");
			Integer quantidade = Integer.parseInt(scanner.nextLine());

			System.out.println("\nBuscando dados...");
			com.example.DoencaChagas.dto.RelatorioCidadeDTO relatorio = sistemaService.gerarRelatorioCidade(idCidade, quantidade);

			System.out.println("\n=======================================================");
			System.out.println("                 RELATÓRIO MUNICIPAL                   ");
			System.out.println("=======================================================");
			if (relatorio.getCidade() != null && relatorio.getCidade().getNome() != null) {
				System.out.println("📍 Cidade: " + relatorio.getCidade().getNome());
			}
			System.out.println("👥 População Total: " + (relatorio.getCidade() != null ? relatorio.getCidade().getPopulacao() : "N/A"));
			System.out.printf(java.util.Locale.US, "📈 Percentual de Casos na População: %.4f%%\n", relatorio.getPercentualCasos());
			
			System.out.println("\n--- [ ESTATÍSTICAS DOS PACIENTES ] ---");
			System.out.println("Casos Confirmados: " + relatorio.getCasosConfirmados());
			System.out.println("Casos Suspeitos:   " + relatorio.getSuspeitos());
			System.out.println("Recuperados:       " + relatorio.getRecuperados());
			System.out.println("Óbitos:            " + relatorio.getObitos());
			System.out.printf(java.util.Locale.US, "Taxa de Mortalidade: %.2f%%\n", relatorio.getTaxaMortalidade());

			System.out.println("\n--- [ ÚLTIMOS PACIENTES REGISTRADOS (" + relatorio.getPacientes().size() + ") ] ---");
			for (Paciente p : relatorio.getPacientes()) {
				System.out.println(" ➜ ID: " + p.getIdPaciente() + " | Idade: " + p.getIdade() + " | Sexo: " + p.getSexo() + " | Status: " + p.getStatusPaciente() + " | Fase: " + p.getFaseDoenca());
			}
			System.out.println("=======================================================\n");

		} catch (Exception e) {
			System.out.println("\n[X] Erro ao gerar o relatório: " + e.getMessage());
		}
	}

	private void compararCidades(Scanner scanner, SistemaService sistemaService) {
		System.out.println("\n--- [ COMPARATIVO ENTRE CIDADES ] ---");
		try {
			System.out.print("Digite o ID da Primeira Cidade (Cidade A): ");
			Integer idCidade1 = Integer.parseInt(scanner.nextLine());

			System.out.print("Digite o ID da Segunda Cidade (Cidade B): ");
			Integer idCidade2 = Integer.parseInt(scanner.nextLine());

			System.out.println("\nBuscando dados e gerando comparativo...");
			java.util.List<com.example.DoencaChagas.dto.EstatisticasCidadeDTO> comparacao = sistemaService.gerarRelatorioComparacao(idCidade1, idCidade2);

			if (comparacao == null || comparacao.size() < 2) {
				System.out.println("\n[X] Não foi possível comparar. Verifique se ambas as cidades possuem pacientes cadastrados.");
				return;
			}

			com.example.DoencaChagas.dto.EstatisticasCidadeDTO cidadeA = comparacao.get(0);
			com.example.DoencaChagas.dto.EstatisticasCidadeDTO cidadeB = comparacao.get(1);

			String nomeA = cidadeA.getCidade() != null ? cidadeA.getCidade().getNome() : "Desconhecida";
			String nomeB = cidadeB.getCidade() != null ? cidadeB.getCidade().getNome() : "Desconhecida";

			// Formatação em tabela simulando um gráfico comparativo
			System.out.println("\n=======================================================================================");
			System.out.println("||                       RELATÓRIO COMPARATIVO DE DOENÇA DE CHAGAS                   ||");
			System.out.println("=======================================================================================");
			System.out.printf("|| %-26s | %-25s | %-25s ||\n", "DADO", "CIDADE A: " + nomeA, "CIDADE B: " + nomeB);
			System.out.println("||----------------------------|---------------------------|---------------------------||");
			
			System.out.printf("|| %-26s | %-25d | %-25d ||\n", "População Total", 
				cidadeA.getCidade().getPopulacao(), 
				cidadeB.getCidade().getPopulacao());
			
			System.out.printf(java.util.Locale.US, "|| %-26s | %-25.4f | %-25.4f ||\n", "% de Casos na População", 
				cidadeA.getPercentualCasos(), 
				cidadeB.getPercentualCasos());

			System.out.printf("|| %-26s | %-25d | %-25d ||\n", "Casos Confirmados", 
				cidadeA.getCasosConfirmados(), 
				cidadeB.getCasosConfirmados());

			System.out.printf("|| %-26s | %-25d | %-25d ||\n", "Casos Suspeitos", 
				cidadeA.getSuspeitos(), 
				cidadeB.getSuspeitos());

			System.out.printf("|| %-26s | %-25d | %-25d ||\n", "Recuperados", 
				cidadeA.getRecuperados(), 
				cidadeB.getRecuperados());

			System.out.printf("|| %-26s | %-25d | %-25d ||\n", "Óbitos", 
				cidadeA.getObitos(), 
				cidadeB.getObitos());

			System.out.printf(java.util.Locale.US, "|| %-26s | %-25.2f | %-25.2f ||\n", "Taxa de Mortalidade (%)", 
				cidadeA.getTaxaMortalidade(), 
				cidadeB.getTaxaMortalidade());
			
			System.out.println("=======================================================================================\n");

		} catch (Exception e) {
			System.out.println("\n[X] Erro ao gerar comparação: " + e.getMessage());
		}
	}

	private void relatorioGeral(Scanner scanner, SistemaService sistemaService) {
		System.out.println("\n--- [ RELATÓRIO GERAL DE TODAS AS CIDADES ] ---");
		try {
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

			System.out.print("Digite a Data Inicial (dd/MM/yyyy): ");
			String dataInicialStr = scanner.nextLine();
			java.time.LocalDate dataInicial = java.time.LocalDate.parse(dataInicialStr, formatter);

			System.out.print("Digite a Data Final (dd/MM/yyyy): ");
			String dataFinalStr = scanner.nextLine();
			java.time.LocalDate dataFinal = java.time.LocalDate.parse(dataFinalStr, formatter);

			System.out.println("\nBuscando dados e gerando relatório no período de " + dataInicialStr + " a " + dataFinalStr + "...\n");
			java.util.List<com.example.DoencaChagas.dto.EstatisticasCidadeDTO> relatorio = sistemaService.gerarRelatorioTodasCidades(dataInicial, dataFinal);

			if (relatorio == null || relatorio.isEmpty()) {
				System.out.println("\n[X] Não há cidades cadastradas no banco de dados.");
				return;
			}

			System.out.println("==================================================================================================================");
			System.out.println("||                                  RELATÓRIO GERAL (TODAS AS CIDADES)                                          ||");
			System.out.println("==================================================================================================================");
			System.out.printf("|| %-20s | %-12s | %-10s | %-10s | %-10s | %-10s | %-15s ||\n", 
				"CIDADE", "POPULAÇÃO", "CONFIRM.", "SUSPEITOS", "RECUPER.", "ÓBITOS", "MORTALIDADE (%)");
			System.out.println("||----------------------|--------------|------------|------------|------------|------------|-----------------||");

			for (com.example.DoencaChagas.dto.EstatisticasCidadeDTO est : relatorio) {
				String nome = est.getCidade() != null && est.getCidade().getNome() != null ? est.getCidade().getNome() : "Desconhecida";
				String pop = est.getCidade() != null && est.getCidade().getPopulacao() != null ? String.valueOf(est.getCidade().getPopulacao()) : "N/A";
				
				System.out.printf(java.util.Locale.US, "|| %-20s | %-12s | %-10d | %-10d | %-10d | %-10d | %-15.2f ||\n",
					nome,
					pop,
					est.getCasosConfirmados(),
					est.getSuspeitos(),
					est.getRecuperados(),
					est.getObitos(),
					est.getTaxaMortalidade());
			}
			System.out.println("==================================================================================================================\n");

		} catch (java.time.format.DateTimeParseException e) {
			System.out.println("\n[X] Erro: Formato de data inválido. Use dd/MM/yyyy.");
		} catch (Exception e) {
			System.out.println("\n[X] Erro ao gerar o relatório geral: " + e.getMessage());
		}
	}

	private void listarCidades(BancoDeDadosDAO bancoDeDadosDAO) {
		System.out.println("\n--- [ CIDADES CADASTRADAS ] ---");
		try {
			java.util.List<Cidade> cidades = bancoDeDadosDAO.findAllCidades();
			if (cidades == null || cidades.isEmpty()) {
				System.out.println(" Nenhuma cidade encontrada no banco de dados.");
			} else {
				System.out.println("ID  | NOME DA CIDADE       | POPULAÇÃO");
				System.out.println("----|----------------------|------------");
				for (Cidade cidade : cidades) {
					System.out.printf("%-3d | %-20s | %d\n", cidade.getIdCidade(), cidade.getNome(), cidade.getPopulacao());
				}
			}
			System.out.println("----------------------------------------\n");
		} catch (Exception e) {
			System.out.println("\n[X] Erro ao buscar cidades: " + e.getMessage());
		}
	}
}
