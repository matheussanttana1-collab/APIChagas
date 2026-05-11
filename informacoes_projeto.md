# Projeto Doença de Chagas

## Introdução do Projeto
O grupo de alunos deverá desenvolver um sistema capaz de receber informações sobre a ocorrência de casos de uma das doenças citadas acima, ou ainda de outras doenças existentes.

O sistema computacional deverá ser desenvolvido utilizando a linguagem de programação Java. Não é obrigatório que no sistema se utilize a parte visual, mas se o grupo achar necessário não tem problemas de incluir.

O grupo de alunos deverá realizar um trabalho utilizando a coleta de dados semanais de 5 cidades da grande São Paulo dos dados fornecidos pelo governo de uma das doenças escolhidas pelo grupo. Estas coletas deverão ser armazenadas e catalogadas.

O grupo deverá fazer um estudo dos dados mais relevantes a serem armazenados e modelar um banco de dados em MySQL que será utilizado pelo sistema para armazenamento e consulta aos dados.

Os dados que serão utilizados para o cadastro no banco de dados podem ser fictícios, ou se preferir busque os dados em informações do governo, ou órgãos responsáveis.

O trabalho deverá possibilitar que os dados cadastrados sejam alterados caso estejam errados.

O trabalho deverá apresentar 3 tipos de relatórios:
1. Relatório de uma cidade – mostrar uma quantidade de coletas escolhida pelo usuário e um percentual de casos em relação a população total.
2. Relatório de comparação entre duas cidades – mostrar um comparativo de dados entre as duas cidades.
3. Relatório de Todas as cidades – mostrar um comparativo de um intervalo de tempo entre todas as 5 cidades.

## Tecnologias Usadas
- Java 25
- Maven (Iniciado)
- Framework: SpringBoot 4.0.6

### Dependências
- Spring Data JPA: Esta é a dependência mais importante. É ela que traz o Hibernate para o projeto e permite que você use os repositórios para salvar dados sem escrever SQL.
- MySQL Driver: O conector nativo que permite que a sua aplicação Java se comunique com o seu banco de dados MySQL.
- Lombok: Ele gera automaticamente seus Getters, Setters e Construtores através de anotações (como @Getter), deixando suas classes de modelo (como a classe Usuario) muito mais limpas.

### Apresentação
- JavaFX

## Estrutura do Projeto

[ SISTEMA DOENÇA DE CHAGAS ]
 │
 ├── 1. APRESENTAÇÃO (JavaFX)
 │   │  Controladores super magros. Capturam o clique e chamam a "Classe Cerebro".
 │   │
 │   └── AppController.java (Controla todas as abas da tela)
 │           │
 │           ▼
 ├── 2. A "CLASSE DE SERVICO" (Camada de Serviço Central)
 │   │  O cérebro do sistema. Tem TODOS os métodos de negócio (salvar, calcular, validar).
 │   │  Não sabe de onde vêm os dados (JavaFX) nem como são salvos (SQL).
 │   │
 │   └── SistemaService.java (@Service)
 │           ├── adicionarNovaColeta()
 │           ├── gerarRelatorio1()
 │           ├── gerarRelatorio2()
 │           └── gerarRelatorio3()
 │           │
 │           ▼
 ├── 3. DAL ÚNICO (Camada de Acesso a Dados)
 │   │  Uma única classe que conversa com o MySQL para QUALQUER tabela.
 │   │
 │   └── BancoDeDadosDAO.java (@Repository)
 │           ├── (Usa o EntityManager do Hibernate)
 │           ├── salvarEntidade(Object obj)
 │           ├── buscarCidades()
 │           └── buscarColetasCustomizadas(...)
 │           │
 │           ▼
 ├── 4. DOMÍNIO (Entidades)
 │   │  As classes de dados que transitam livremente entre o DAO, Service e JavaFX.
 │   │
 │   ├── Cidade.java (@Entity)
 │   ├── ColetaSemanal.java (@Entity)
 │   └── Paciente.java (@Entity)
 │
 └── 🗄️ BANCO DE DADOS (MySQL)

## Entidades (Baseado na Modelagem de Dados)

### 1. Cidade (`cidades`)
- `id_cidade` INT (Chave Primária)
- `nome` VARCHAR(100)
- `populacao` INT
- `regiao` VARCHAR(100)

### 2. Coleta Semanal (`coletas`)
- `id_coleta` INT (Chave Primária)
- `id_cidade` INT (Chave Estrangeira -> cidades)
- `data_inicio_semana` DATE
- `data_fim_semana` DATE
- `casos_novos` INT
- `casos_confirmados` INT
- `casos_suspeitos` INT
- `obitos` INT
- `recuperados` INT

### 3. Paciente (`pacientes`)
- `id_paciente` INT (Chave Primária)
- `idade` INT
- `sexo` CHAR(1)
- `fase_doenca` VARCHAR(20)
- `forma_clinica` VARCHAR(30)
- `via_transmissao` VARCHAR(30)
- `status_paciente` VARCHAR(20)
- `data_registro` DATE
- `data_atualizacao` DATE
- `id_cidade` INT (Chave Estrangeira -> cidades)
